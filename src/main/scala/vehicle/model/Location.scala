package vehicle.model

case class Location(longitude: BigDecimal, latitude: BigDecimal)

object Location {
  val initial = Location(0,0)
}
