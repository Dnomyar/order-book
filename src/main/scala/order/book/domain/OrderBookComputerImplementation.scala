package order.book.domain

import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.ports.{OrderBookChangeRequestFetcher, OrderBookComputer}
import order.book.domain.projections.OrderBookProjection

import scala.util.Try

class OrderBookComputerImplementation(orderBookChangeRequestFetcher: OrderBookChangeRequestFetcher) extends OrderBookComputer {

  def compute(filename: String, tickSize: TickSize, bookDepth: Int): Try[List[OrderBookProjection]] =
    for{
      changes <- orderBookChangeRequestFetcher.fetch(filename)
      book <- computeBook(changes, tickSize, bookDepth)
    } yield book.describeOrderBook(tickSize)


  def computeBook(changes: Iterator[OrderBookChangeRequest], tickSize: TickSize, bookDepth: Int): Try[OrderBook] =
    changes
      .filter(_.priceLevelIndex <= bookDepth)
      .foldLeft(Try(OrderBook(bookDepth))){
        case (bookTry, order) => bookTry.flatMap(_.applyChange(order))
      }


}
