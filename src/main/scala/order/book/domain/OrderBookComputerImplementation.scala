package order.book.domain

import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.ports.OrderBookComputer
import order.book.domain.projections.OrderBookProjection

import scala.util.Try

class OrderBookComputerImplementation extends OrderBookComputer {

  def compute(updateOrderBookCommands: Iterator[UpdateOrderBookCommand], tickSize: TickSize, bookDepth: Int): Try[List[OrderBookProjection]] =
    updateOrderBookCommands
      .filter(_.priceLevelIndex <= bookDepth)
      .foldLeft(Try(OrderBook(bookDepth))){
        case (bookTry, order) => bookTry.flatMap(_.applyChange(order))
      }
      .map(_.describeOrderBook(tickSize))


}
