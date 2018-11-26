package order.book.domain.result

import order.book.domain.{OrderBookOrder, Quantity, TickSize}

case class OrderBookDescription(bidPrice: Double,
                                bidQuantity: Quantity,
                                askPrice: Double,
                                askQuantity: Quantity)


object OrderBookDescription {

  def fromOrders(tickSize: TickSize)(bidOrder: OrderBookOrder, askOrder: OrderBookOrder): OrderBookDescription =
    (bidOrder.describe(tickSize), askOrder.describe(tickSize)) match {
      case ((bidPrice, bidQuantity), (askPrice, askQuantity)) => OrderBookDescription(bidPrice, bidQuantity, askPrice, askQuantity)
    }

}
