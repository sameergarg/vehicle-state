package vehicle.state

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.IOResult
import akka.stream.scaladsl._
import vehicle.state.model.JsonMarshaller._
import vehicle.state.model._

import java.nio.file.{Path, Paths}
import scala.concurrent._

object VehicleApp extends App {
  implicit val system: ActorSystem = ActorSystem("VehicleState")
  implicit val ec = system.dispatcher
  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

  private val batteryDataFilePath: Path = Paths.get("batteryData.json")
  private val batteryData: Source[BatteryData, Future[IOResult]] = FileIO.fromPath(batteryDataFilePath).via(jsonStreamingSupport.framingDecoder).mapAsync(1)(bytes => Unmarshal(bytes).to[BatteryData])
  private val batteryNoLocationSource: Source[BatteryWithLocation, Future[IOResult]] = batteryData.map(BatteryWithLocation.withBatteryData)

  private val locationDataFilePath: Path = Paths.get("locationData.json")
  private val locationData: Source[Location, Future[IOResult]] = FileIO.fromPath(locationDataFilePath).via(jsonStreamingSupport.framingDecoder).mapAsync(1)(bytes => Unmarshal(bytes).to[Location])
  private val locationNoBatterySource: Source[BatteryWithLocation, Future[IOResult]] = locationData.map(BatteryWithLocation.withLocation)

  private val batteryWithLocation: Source[BatteryWithLocation, NotUsed] = Source.combine(batteryNoLocationSource, locationNoBatterySource)(Merge(_))

  private val vehicleState: Sink[BatteryWithLocation, Future[VehicleState]] =  Sink.fold(VehicleState.initial)((acc, batteryWithLocation) => VehicleState.update(acc, batteryWithLocation))

  batteryWithLocation.runWith(vehicleState).onComplete { finalState =>
    finalState.foreach(vehicle => println(vehicle.state))
    system.terminate()
  }

}