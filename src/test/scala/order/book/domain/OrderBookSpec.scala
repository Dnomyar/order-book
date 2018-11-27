package order.book.domain

import order.book.domain.commands.OrderBookInstruction.New
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.projections.OrderBookProjection
import org.scalatest.{Matchers, WordSpec}

class OrderBookSpec extends WordSpec with Matchers {


  "It" should {
    "be possible to apply a change to the bid size" in {
      val book = OrderBook(1)
        .applyChange(UpdateOrderBookCommand(
          New,
          Bid,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.get.asks.orders should be (Vector(EmptyOrder))
      book.get.bids.orders should be (Vector(Order(TickPrice(5), Quantity(30))))
    }

    "be possible to apply a change to the ask size" in {
      val book = OrderBook(1)
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.get.asks.orders should be (Vector(Order(TickPrice(5), Quantity(30))))
      book.get.bids.orders should be (Vector(EmptyOrder))
    }

    "be possible to describe a book" in {
      OrderBook(1)
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        )).get.project(TickSize(10)) should be (List(
        OrderBookProjection(0, Quantity(0), 50, Quantity(30))
      ))
    }
  }

}
