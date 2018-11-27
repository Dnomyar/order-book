package order.book.domain.projections

import order.book.domain.{OrderBookOrder, Quantity, TickSize}

case class OrderBookProjection(bidPrice: Double,
                               bidQuantity: Quantity,
                               askPrice: Double,
                               askQuantity: Quantity)


object OrderBookProjection {

  def fromOrders(tickSize: TickSize)(bidOrder: OrderBookOrder, askOrder: OrderBookOrder): OrderBookProjection =
    (bidOrder.describe(tickSize), askOrder.describe(tickSize)) match {
      case ((bidPrice, bidQuantity), (askPrice, askQuantity)) => OrderBookProjection(bidPrice, bidQuantity, askPrice, askQuantity)
    }

}
