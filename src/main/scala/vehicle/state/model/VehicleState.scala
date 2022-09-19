package vehicle.state.model

case class VehicleState(batteryPercentage: BatteryPercentage, direction: DirectionState) {
  def state() = s"Battery percentage =  ${batteryPercentage.value.setScale(2, BigDecimal.RoundingMode.HALF_UP)} %, direction: ${direction.currentDirection.getOrElse("")}"
}


object VehicleState {
  val initial = VehicleState(BatteryPercentage.zero, DirectionState.start)

  def update(oldValue: VehicleState, batteryWithLocation: BatteryWithLocation): VehicleState = (batteryWithLocation.batteryData, batteryWithLocation.location) match {
    case (Some(batteryData), None) => oldValue.copy(batteryPercentage = BatteryPercentage(batteryData))
    case (None, Some(location)) => oldValue.copy(direction = DirectionState(location, Direction.direction(oldValue.direction.currentLocation, location)))
    case (Some(batteryData), Some(location)) =>
      oldValue.copy(
        batteryPercentage = BatteryPercentage(batteryData),
        direction = DirectionState(location, Direction.direction(oldValue.direction.currentLocation, location))
      )
    case (None, None) => oldValue
  }
}
