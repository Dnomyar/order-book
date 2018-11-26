package order.book.domain

import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.result.OrderBookDescription

class OrderBookUpdater {

  def update(changes: Iterator[OrderBookChangeRequest], tickSize: TickSize, bookDepth: Int): List[OrderBookDescription] =
    changes
      .filter(_.priceLevelIndex <= bookDepth)
      .foldLeft(OrderBook(bookDepth))(_.applyChange(_))
      .describeOrderBook(tickSize)


}
