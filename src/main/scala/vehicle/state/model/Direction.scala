package vehicle.state.model

sealed trait Direction

object Direction {

  def direction(from: Location, to: Location): Option[Direction] = (to.latitude - from.latitude, to.longitude - from.longitude) match {
    case (lat, lon) if lat > 0 && lon == 0 => Some(East)
    case (lat, lon) if lat < 0 && lon == 0 => Some(West)
    case (lat, lon) if lat == 0 && lon > 0 => Some(North)
    case (lat, lon) if lat == 0 && lon < 0 => Some(South)
    case (lat, lon) if lat > 0 && lon > 0 => Some(NorthEast)
    case (lat, lon) if lat > 0 && lon < 0 => Some(SouthEast)
    case (lat, lon) if lat < 0 && lon > 0 => Some(NorthWest)
    case (lat, lon) if lat < 0 && lon < 0 => Some(SouthWest)
    case (lat, lon) if lat == 0 && lon == 0 => None
  }

  case object East extends Direction
  case object West extends Direction
  case object North extends Direction
  case object South extends Direction
  case object NorthEast extends Direction
  case object NorthWest extends Direction
  case object SouthWest extends Direction
  case object SouthEast extends Direction
}
