package order.book.domain

import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.OrderBookSide._
import order.book.domain.change.request.OrderBookInstruction.New
import order.book.domain.change.request.OrderBookSide.{Ask, Bid}
import order.book.domain.result.OrderBookDescription
import org.scalatest.{Matchers, WordSpec}

class OrderBookSpec extends WordSpec with Matchers {


  "It" should {
    "be possible to apply a change to the bid size" in {
      val book = OrderBook(1)
        .applyChange(OrderBookChangeRequest(
          New,
          Bid,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.asks.orders should be (Vector(EmptyOrder))
      book.bids.orders should be (Vector(Order(TickPrice(5), Quantity(30))))
    }

    "be possible to apply a change to the ask size" in {
      val book = OrderBook(1)
        .applyChange(OrderBookChangeRequest(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.asks.orders should be (Vector(Order(TickPrice(5), Quantity(30))))
      book.bids.orders should be (Vector(EmptyOrder))
    }

    "be possible to describe a book" in {
      OrderBook(1)
        .applyChange(OrderBookChangeRequest(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        )).describeOrderBook(TickSize(10)) should be (List(
        OrderBookDescription(0, Quantity(0), 50, Quantity(30))
      ))
    }
  }

}
