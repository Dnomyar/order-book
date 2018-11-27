package order.book.domain

import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.commands.OrderBookInstruction._
import order.book.domain.commands.OrderBookSide._
import order.book.domain.ports.OrderBookComputer
import order.book.domain.projections.OrderBookProjection
import org.scalatest.{Matchers, WordSpec}

import scala.util.Success

class OrderBookComputerSpec extends WordSpec with Matchers {

  val orderBookComputer: OrderBookComputer = new OrderBookComputerImplementation

  "It" should {
    "be possible to apply several change requests to the book (with the book depth constraint 2)" in {

      orderBookComputer.compute(Iterator(
        UpdateOrderBookCommand(New, Bid, 1, TickPrice(5), Quantity(30)),
        UpdateOrderBookCommand(New, Bid, 2, TickPrice(4), Quantity(40)),
        UpdateOrderBookCommand(New, Ask, 1, TickPrice(6), Quantity(10)),
        UpdateOrderBookCommand(New, Ask, 2, TickPrice(7), Quantity(10)),
        UpdateOrderBookCommand(Update, Ask, 2, TickPrice(7), Quantity(20)),
        UpdateOrderBookCommand(Update, Bid, 1, TickPrice(5), Quantity(40))
      ), TickSize(10), 2) should be (Success(List(
        OrderBookProjection(50, Quantity(40), 60, Quantity(10)),
        OrderBookProjection(40, Quantity(40), 70, Quantity(20))
      )))

    }

    "be possible to apply several change requests to the book (with the book depth constraint 1)" in {

      orderBookComputer.compute(Iterator(
        UpdateOrderBookCommand(New, Bid, 1, TickPrice(5), Quantity(30)),
        UpdateOrderBookCommand(New, Bid, 2, TickPrice(4), Quantity(40)),
        UpdateOrderBookCommand(New, Ask, 1, TickPrice(6), Quantity(10)),
        UpdateOrderBookCommand(New, Ask, 2, TickPrice(7), Quantity(10)),
        UpdateOrderBookCommand(Update, Ask, 2, TickPrice(7), Quantity(20)),
        UpdateOrderBookCommand(Update, Bid, 1, TickPrice(5), Quantity(40))
      ), TickSize(10), 1) should be (Success(List(
        OrderBookProjection(50, Quantity(40), 60, Quantity(10))
      )))

    }
  }
}
