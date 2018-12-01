package order.book.domain

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import order.book.application.ParseActor
import order.book.application.ParseActor.Finished
import order.book.domain.OrderBookUpdaterActor.Result
import order.book.domain.commands.UpdateOrderBookCommand

class OrderBookUpdaterActor(bookDepth: Int, tickSize: TickSize) extends Actor with ActorLogging {

  log.info("Started")

  var orderBook: OrderBook = OrderBook(bookDepth)

  var numberOfCommandReceived = 0

  override def receive: Receive = {
    case cmd: UpdateOrderBookCommand =>
      numberOfCommandReceived += 1
      orderBook = orderBook.applyChange(cmd).get
    case Finished =>
      println(numberOfCommandReceived)
      println(
        orderBook.project(tickSize)
        .map(p => s"${p.bidPrice},${p.bidQuantity.value},${p.askPrice},${p.askQuantity.value}")
        .mkString("\n")
      )
      sender() ! PoisonPill
      self ! PoisonPill
  }
}


object OrderBookUpdaterActor {

  case class Result(orderBook: OrderBook)

  def props(bookDepth: Int, tickSize: TickSize): Props = {
    Props(new OrderBookUpdaterActor(bookDepth, tickSize))
  }
}