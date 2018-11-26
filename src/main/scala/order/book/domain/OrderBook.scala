package order.book.domain

import order.book.domain.change.request.OrderBookSide.{Ask, Bid}
import order.book.domain.change.request.{OrderBookChangeRequest, OrderBookInstruction}
import order.book.domain.result.OrderBookDescription



case class OrderBook private[OrderBook] (bids: OrderBookSide, asks: OrderBookSide) {

  def applyChange(changeRequest: OrderBookChangeRequest): OrderBook =
    updateSide(changeRequest.side)(_.applyOrderChange(
      changeRequest.instruction,
      changeRequest.priceLevelIndex,
      changeRequest.price,
      changeRequest.quantity
    ))


  private def updateSide(side: change.request.OrderBookSide)(updateSideFunction: OrderBookSide => OrderBookSide): OrderBook = side match {
    case Bid => copy(bids = updateSideFunction(bids))
    case Ask => copy(asks = updateSideFunction(asks))
  }


  def describeOrderBook(tickSize: TickSize) : List[OrderBookDescription] =
    bids.orders.zip(asks.orders)
      .map((OrderBookDescription.fromOrders(tickSize) _).tupled).toList

}


object OrderBook {
  def apply(bookDepth: Int): OrderBook =
    new OrderBook(OrderBookSide(bookDepth), OrderBookSide(bookDepth))
}



