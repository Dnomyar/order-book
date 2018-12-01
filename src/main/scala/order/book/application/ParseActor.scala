package order.book.application

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import order.book.application.ParseActor.{Finished, ParseLine}
import order.book.domain.OrderBookUpdaterActor.Result

class ParseActor(next: ActorRef) extends Actor with ActorLogging {

  log.info("Started")

  val parser: ParseUpdateOrderBookCommand = new ParseUpdateOrderBookCommandStateMonad
  var numberOfCommandReceived = 0

  override def receive: Receive = {
    case ParseLine(str) =>
      numberOfCommandReceived += 1
      next ! parser.parse(str)
    case Finished =>
      println(numberOfCommandReceived)
      next ! Finished
  }
}


object ParseActor {

  case class ParseLine(line: String)
  case object Finished

  def props(next: ActorRef): Props = Props(new ParseActor(next))

}