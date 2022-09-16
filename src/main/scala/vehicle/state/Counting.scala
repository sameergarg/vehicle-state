package vehicle.state

// step 1
import akka.stream.scaladsl._

// step 2
import akka.actor.ActorSystem
import akka.{Done, NotUsed}

import scala.concurrent._

object Counting extends App {
  implicit val system: ActorSystem = ActorSystem("Counting")
  implicit val ec = system.dispatcher
  val source: Source[Int, NotUsed] = Source(1 to 100)

  val done: Future[Done] = source.runForeach(i => println(i))

  done.onComplete(_ => system.terminate())
}
