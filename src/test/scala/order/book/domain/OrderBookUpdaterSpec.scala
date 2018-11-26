package order.book.domain

import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.change.request.OrderBookInstruction._
import order.book.domain.change.request.OrderBookSide._
import order.book.domain.result.OrderBookDescription
import org.scalatest.{Matchers, WordSpec}

class OrderBookUpdaterSpec extends WordSpec with Matchers {
  "It" should {
    "be possible to apply several change requests to the book (with the book depth constraint 2)" in {

      (new OrderBookUpdater).update(List(
        OrderBookChangeRequest(New, Bid, 1, TickPrice(5), Quantity(30)),
        OrderBookChangeRequest(New, Bid, 2, TickPrice(4), Quantity(40)),
        OrderBookChangeRequest(New, Ask, 1, TickPrice(6), Quantity(10)),
        OrderBookChangeRequest(New, Ask, 2, TickPrice(7), Quantity(10)),
        OrderBookChangeRequest(Update, Ask, 2, TickPrice(7), Quantity(20)),
        OrderBookChangeRequest(Update, Bid, 1, TickPrice(5), Quantity(40))
      ), TickSize(10), 2) should be (List(
        OrderBookDescription(50, Quantity(40), 60, Quantity(10)),
        OrderBookDescription(40, Quantity(40), 70, Quantity(20))
      ))

    }

    "be possible to apply several change requests to the book (with the book depth constraint 1)" in {

      (new OrderBookUpdater).update(List(
        OrderBookChangeRequest(New, Bid, 1, TickPrice(5), Quantity(30)),
        OrderBookChangeRequest(New, Bid, 2, TickPrice(4), Quantity(40)),
        OrderBookChangeRequest(New, Ask, 1, TickPrice(6), Quantity(10)),
        OrderBookChangeRequest(New, Ask, 2, TickPrice(7), Quantity(10)),
        OrderBookChangeRequest(Update, Ask, 2, TickPrice(7), Quantity(20)),
        OrderBookChangeRequest(Update, Bid, 1, TickPrice(5), Quantity(40))
      ), TickSize(10), 1) should be (List(
        OrderBookDescription(50, Quantity(40), 60, Quantity(10))
      ))

    }
  }
}
