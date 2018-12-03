package order.book.domain

import order.book.domain.commands.OrderBookInstruction.New
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.projections.OrderBookProjection
import org.scalatest.{Matchers, WordSpec}

class OrderBookSpec extends WordSpec with Matchers {


  "It" should {
    "be possible to apply a change to the bid size" in {
      val book = OrderBook.empty
        .applyChange(UpdateOrderBookCommand(
          New,
          Bid,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.get.asks.getOrders should be (List.empty)
      book.get.bids.getOrders should be (List(Order(TickPrice(5), Quantity(30))))
    }

    "be possible to apply a change to the ask size" in {
      val book = OrderBook.empty
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        ))

      book.get.asks.getOrders should be (List(Order(TickPrice(5), Quantity(30))))
      book.get.bids.getOrders should be (List.empty)
    }

    "be possible to describe a book" in {
      OrderBook.empty
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(5),
          Quantity(30)
        )).get.project(1, TickSize(10)) should be (List(
        OrderBookProjection(0, Quantity(0), 50, Quantity(30))
      ))
    }

    "work with a simple example" in {

      /*
      INPUT:
      N A 1 1 10
	    N A 1 2 20

	    OUTPUT:
	    0.0,0,20.0,20
	    0.0,0,10.0,10
       */

      OrderBook.empty
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(1),
          Quantity(10)
        )).get
        .applyChange(UpdateOrderBookCommand(
          New,
          Ask,
          1,
          TickPrice(2),
          Quantity(20)
        ))
        .get.project(1, TickSize(10)) should be (List(
        OrderBookProjection(0, Quantity(0), 20, Quantity(20)),
        OrderBookProjection(0, Quantity(0), 10, Quantity(10))
      ))
    }
  }

}
