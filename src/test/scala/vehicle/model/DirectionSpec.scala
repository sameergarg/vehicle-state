package vehicle.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import vehicle.model.Direction._

class DirectionSpec extends AnyWordSpec with Matchers {
  // east of equator
  val east = Location(latitude = BigDecimal(3.202778), longitude = BigDecimal(73.22068))

  val northEast = Location(latitude = BigDecimal(5.202778), longitude = BigDecimal(83.22068))

  val northWest = Location(latitude = BigDecimal(1.202778), longitude = BigDecimal(83.22068))

  val southEast = Location(latitude = BigDecimal(5.202778), longitude = BigDecimal(53.22068))

  val southWest = Location(latitude = BigDecimal(1.202778), longitude = BigDecimal(53.22068))
  // west of equator
  val west = Location(latitude = BigDecimal(-3.202778), longitude = BigDecimal(73.22068))

  // north of prime meridian
  val north = Location(latitude = BigDecimal(3.202778), longitude = BigDecimal(73.22068))
  // south of prime meridian
  val south = Location(latitude = BigDecimal(3.202778), longitude = BigDecimal(-73.22068))


  "Direction" should {
    "determine direction of movement" when {

      "moving towards west" in {
        Direction.direction(from = east, to = west) shouldBe Some(West)
      }

      "moving towards east" in {
        Direction.direction(from = west, to = east) shouldBe Some(East)
      }

      "moving towards south" in {
        Direction.direction(from = north, to = south) shouldBe Some(South)
      }

      "moving towards south east" in {
        Direction.direction(from = east, to = southEast) shouldBe Some(SouthEast)
      }

      "moving towards south west" in {
        Direction.direction(from = east, to = southWest) shouldBe Some(SouthWest)
      }

      "moving towards north" in {
        Direction.direction(from = south, to = north) shouldBe Some(North)
      }

      "moving towards north east" in {
        Direction.direction(from = east, to = northEast) shouldBe Some(NorthEast)
      }

      "moving towards north west" in {
        Direction.direction(from = east, to = northWest) shouldBe Some(NorthWest)
      }

      "as stationary" in {
        Direction.direction(from = east, to = east) shouldBe None
      }
    }
  }

}
