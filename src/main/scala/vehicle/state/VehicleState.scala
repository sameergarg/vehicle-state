package vehicle.state

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import vehicle.state.model.{BatteryData, BatteryPercentage, Direction, DirectionState, Location}

import scala.concurrent._

object VehicleState extends App {
  implicit val system: ActorSystem = ActorSystem("VehicleState")
  implicit val ec = system.dispatcher

  val batteryData: Source[BatteryData, NotUsed] = ??? // TODO read from file or http
  val batteryPercentage: Flow[BatteryData, BatteryPercentage, NotUsed] = Flow.fromFunction(BatteryPercentage(_))

  val sourceLocation: Source[Location, NotUsed] = ???
  val direction: Sink[Location, Future[DirectionState]] = Sink.fold(DirectionState.start)((acc, location) => DirectionState(location, Direction.direction(acc.currentLocation, location)))


}
