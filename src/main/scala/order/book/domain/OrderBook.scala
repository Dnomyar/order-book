package order.book.domain

import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.projections.OrderBookProjection

import scala.util.Try



case class OrderBook private[OrderBook] (bids: OrderBookSide, asks: OrderBookSide) {

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


  def project(bookSize: Int, tickSize: TickSize) : List[OrderBookProjection] =
    (0 until bookSize)
      .zipAll(bids.getOrders, 0, EmptyOrder).map(_._2)
      .zipAll(asks.getOrders, EmptyOrder, EmptyOrder)
      .map((OrderBookProjection.fromOrders(tickSize) _).tupled).toList

}


object OrderBook {
  def empty: OrderBook = new OrderBook(OrderBookSide.empty, OrderBookSide.empty)
}



