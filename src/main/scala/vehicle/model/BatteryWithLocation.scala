package vehicle.model

case class BatteryWithLocation(batteryData: Option[BatteryData], location: Option[Location])

object BatteryWithLocation {

  def withBatteryData(batteryData: BatteryData) = BatteryWithLocation(Some(batteryData), location = None)

  def withLocation(location: Location) = BatteryWithLocation(batteryData = None, location = Some(location))
}