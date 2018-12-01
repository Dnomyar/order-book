package order.book.domain

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.ports.OrderBookComputer
import order.book.domain.projections.OrderBookProjection

import scala.concurrent.Future
import scala.util.{Success, Try}

class OrderBookComputerImplementation extends OrderBookComputer {

  // O(|updateOrderBookCommands| + book_depth)
  def compute(updateOrderBookCommands: Iterator[UpdateOrderBookCommand], tickSize: TickSize, bookDepth: Int): Try[List[OrderBookProjection]] =
    updateOrderBookCommands
      .filter(_.priceLevelIndex <= bookDepth)
      .foldLeft(Try(OrderBook(bookDepth))){
        case (bookTry, order) => bookTry.flatMap(_.applyChange(order))
      }
      .map(_.project(tickSize))

  override def computeAkkaStream(updateOrderBookCommands: Source[UpdateOrderBookCommand, Future[IOResult]], tickSize: TickSize, bookDepth: Int): Source[OrderBookProjection, Future[IOResult]] = {
    updateOrderBookCommands
      .filter(_.priceLevelIndex <= bookDepth)
      .fold(Try(OrderBook(bookDepth))){
        case (bookTry, order) => bookTry.flatMap(_.applyChange(order))
      }
      .collect{
        case Success(value) => value
      }
      .mapConcat(_.project(tickSize))
  }
}
