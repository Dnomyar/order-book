package order.book.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object AkkaObjects {

  implicit val system: ActorSystem = ActorSystem("order-book")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

}
