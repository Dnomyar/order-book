package order.book.domain

import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.projections.OrderBookProjection

import scala.util.Try



case class OrderBook private[OrderBook] (bids: OrderBookSide, asks: OrderBookSide) {

  // O(1)
  def applyChange(changeRequest: UpdateOrderBookCommand): Try[OrderBook] =
    updateSide(changeRequest.side)(_.applyOrderChange(
      changeRequest.instruction,
      changeRequest.priceLevelIndex,
      changeRequest.price,
      changeRequest.quantity
    ))


  private def updateSide(side: commands.OrderBookSide)(updateSideFunction: OrderBookSide => Try[OrderBookSide]): Try[OrderBook] = side match {
    case Bid => updateSideFunction(bids).map(newBids => copy(bids = newBids))
    case Ask => updateSideFunction(asks).map(newAsks => copy(asks = newAsks))
  }


  // O(book_depth)
  def project(tickSize: TickSize) : List[OrderBookProjection] =
    bids.orders.zip(asks.orders)
      .map((OrderBookProjection.fromOrders(tickSize) _).tupled).toList

}


object OrderBook {
  def apply(bookDepth: Int): OrderBook =
    new OrderBook(OrderBookSide(bookDepth), OrderBookSide(bookDepth))
}



