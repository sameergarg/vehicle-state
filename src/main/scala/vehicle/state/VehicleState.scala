package vehicle.state

// step 1
import akka.stream._
import akka.stream.scaladsl._

// step 2
import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths

object VehicleState extends App {
  implicit val system: ActorSystem = ActorSystem("VehicleState")
  implicit val ec = system.dispatcher
  val source: Source[Int, NotUsed] = Source(1 to 100)

  val done: Future[Done] = source.runForeach(i => println(i))

  done.onComplete(_ => system.terminate())
}
