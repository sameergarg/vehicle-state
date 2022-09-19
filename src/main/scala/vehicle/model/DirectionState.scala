package vehicle.model

case class DirectionState(currentLocation: Location, currentDirection: Option[Direction])

object DirectionState {
  val start: DirectionState = DirectionState(Location.initial, None)
}
