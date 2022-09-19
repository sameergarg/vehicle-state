package vehicle.repository

import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape
import vehicle.model.BatteryStatus
import vehicle.repository.H2BatteryStatusRepository.batteryStatus

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait ConfigurationRepository {

  // TODO Error handling
  def batteryHealth: Future[Set[BatteryStatus]]
}

class H2BatteryStatusRepository(db: H2Profile.backend.Database) extends ConfigurationRepository {

  override def batteryHealth: Future[Set[BatteryStatus]] = db.run(batteryStatus.result).map(_.map {
    case (status, lower, upper) => BatteryStatus(status, lower, upper)
    }.toSet
  )
}

object H2BatteryStatusRepository {
  class BatteryStatus(tag: Tag) extends Table[(String, Int, Int)](tag, "battery_status") {
    def status: Rep[String] = column[String]("status")
    def lower: Rep[Int] = column[Int]("lower")
    def upper: Rep[Int] = column[Int]("upper")
    override def * : ProvenShape[(String, Int, Int)] = (status, lower, upper)
  }

  val batteryStatus = TableQuery[BatteryStatus]
}