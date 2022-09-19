package vehicle.model

case class Vehicle(direction: Direction, batteryStatus: String) {
  def state() = s"Battery status =  $batteryStatus , direction: $direction"
}

object Vehicle {
  def safeApply(maybeDirection: Option[Direction], maybeBatteryStatus: Option[String]): Option[Vehicle] =
    for {
      direction <- maybeDirection
      batteryStatus <- maybeBatteryStatus
    } yield Vehicle(direction, batteryStatus)
}
