package vehicle.state

// step 1
import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString

import java.nio.file.Paths

// step 2
import akka.actor.ActorSystem
import akka.{Done, NotUsed}

import scala.concurrent._

object Factorials extends App {
  implicit val system: ActorSystem = ActorSystem("Factorials")
  implicit val ec = system.dispatcher
  val source: Source[Int, NotUsed] = Source(1 to 100)

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  val result: Future[IOResult] =
    factorials.map(num => ByteString(s"$num\n")).runWith(FileIO.toPath(Paths.get("factorials.txt")))

  result.onComplete(_ => system.terminate())
}
