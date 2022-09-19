package vehicle.state.model

case class BatteryPercentage(value: BigDecimal)

object BatteryPercentage {

  val zero = BatteryPercentage(0)

  def apply(batteryData: BatteryData): BatteryPercentage =
    BatteryPercentage((batteryData.stateOfCharge / batteryData.batteryCapacity) * 100)

}
