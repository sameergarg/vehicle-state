package vehicle.state.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json._

object JsonMarshaller extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val batteryDataFormat: RootJsonFormat[BatteryData] = jsonFormat2(BatteryData)

  implicit val locationFormat: RootJsonFormat[Location] = jsonFormat2(Location.apply)
}
