package vehicle

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.IOResult
import akka.stream.scaladsl.{Merge, Sink, Source, StreamConverters}
import slick.dbio.DBIO
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._
import vehicle.model.JsonMarshaller._
import vehicle.model._
import vehicle.repository.H2BatteryStatusRepository
import vehicle.repository.H2BatteryStatusRepository.batteryStatus

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object VehicleApp extends App {
  implicit val system: ActorSystem = ActorSystem("VehicleState")
  implicit val ec = system.dispatcher
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

  val db: H2Profile.backend.Database = Database.forConfig("h2mem1")

  //setup db
  val setupDB = DBIO.seq(
    batteryStatus.schema.create,
      batteryStatus +=("Red", 0, 33),
      batteryStatus +=("Yellow", 34, 66),
      batteryStatus +=("Green", 67, 100)
  )
  Await.result(db.run(setupDB), Duration.Inf)

  val batteryStatusRepository = new H2BatteryStatusRepository(db)

  private val batteryDataStream = getClass.getResourceAsStream("/batteryData.json")
  private val batteryData: Source[BatteryData, Future[IOResult]] = StreamConverters.fromInputStream(() => batteryDataStream).via(jsonStreamingSupport.framingDecoder).mapAsync(1)(bytes => Unmarshal(bytes).to[BatteryData])
  private val batteryNoLocationSource: Source[BatteryWithLocation, Future[IOResult]] = batteryData.map(BatteryWithLocation.withBatteryData)

  private val locationDataStream = getClass.getResourceAsStream("/locationData.json")
  private val locationData: Source[Location, Future[IOResult]] = StreamConverters.fromInputStream(() => locationDataStream).via(jsonStreamingSupport.framingDecoder).mapAsync(1)(bytes => Unmarshal(bytes).to[Location])
  private val locationNoBatterySource: Source[BatteryWithLocation, Future[IOResult]] = locationData.map(BatteryWithLocation.withLocation)

  private val batteryWithLocation: Source[BatteryWithLocation, NotUsed] = Source.combine(batteryNoLocationSource, locationNoBatterySource)(Merge(_))

  // TODO use graph dsl
  private val vehicleState: Sink[BatteryWithLocation, Future[VehicleState]] = Sink.fold(VehicleState.initial)((acc, batteryWithLocation) => VehicleState.update(acc, batteryWithLocation))

  for {
    finalState <- batteryWithLocation.runWith(vehicleState)
    batteryPercentage = finalState.batteryPercentage.value
    batteryStatuses <- batteryStatusRepository.batteryHealth
    batteryStatus = batteryStatuses.find(s => batteryPercentage >= s.lower && batteryPercentage <= s.upper)
    mayBeVehicle = Vehicle.safeApply(finalState.direction.currentDirection, batteryStatus.map(_.status))
    _ = mayBeVehicle.fold(println("Unable to determine vehicle direction and battery status"))(v => println(v.state))
  } yield {
    try {
      batteryDataStream.close()
      locationDataStream.close()
    } finally {
      system.terminate()
    }
  }

}
