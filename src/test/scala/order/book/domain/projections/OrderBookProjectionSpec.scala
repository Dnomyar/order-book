package order.book.domain.projections

import order.book.domain._
import org.scalatest.{Matchers, WordSpec}

class OrderBookProjectionSpec extends WordSpec with Matchers {

  "It" should {
    "be possible to build an OrderBookProjection from the prise size, the bid and the ask orders" in {
      OrderBookProjection.fromOrders(TickSize(10))(Order(TickPrice(5), Quantity(45)), Order(TickPrice(3), Quantity(23))) should be (
        OrderBookProjection(50, Quantity(45), 30, Quantity(23)))
    }

    "be possible to build an OrderBookProjection from the prise size, the bid and the ask orders even if the bid is an empty order" in {
      OrderBookProjection.fromOrders(TickSize(10))(EmptyOrder, Order(TickPrice(3), Quantity(23))) should be (
        OrderBookProjection(0, Quantity(0), 30, Quantity(23)))
    }
  }

}
