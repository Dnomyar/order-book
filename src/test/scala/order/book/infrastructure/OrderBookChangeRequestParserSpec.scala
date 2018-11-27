package order.book.infrastructure

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.change.request.OrderBookInstruction.{New, Update}
import order.book.domain.change.request.OrderBookSide.{Ask, Bid}
import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Success}

class OrderBookChangeRequestParserSpec extends WordSpec with Matchers {

  "It" should {
    "be possible to parse a file if the file is ok" in {
      (new OrderBookChangeRequestParserAdapter).parseFile(
        """N B 1 5 30
          |N B 2 4 40
          |N A 1 6 10
          |N A 2 7 10
          |U A 2 7 20
          |U B 1 5 40""".stripMargin.toInputStream).map(_.toList) should be (Success(List(
        OrderBookChangeRequest(New, Bid, 1, TickPrice(5), Quantity(30)),
        OrderBookChangeRequest(New, Bid, 2, TickPrice(4), Quantity(40)),
        OrderBookChangeRequest(New, Ask, 1, TickPrice(6), Quantity(10)),
        OrderBookChangeRequest(New, Ask, 2, TickPrice(7), Quantity(10)),
        OrderBookChangeRequest(Update, Ask, 2, TickPrice(7), Quantity(20)),
        OrderBookChangeRequest(Update, Bid, 1, TickPrice(5), Quantity(40))
      )))
    }

    "be possible to parse a file with errors" in {
      (new OrderBookChangeRequestParserAdapter).parseFile(
        """N B 1 5 30
          |N B 2 4 40
          |N A 1 6 10
          |N A 2 7 10
          |U  2 7 20
          |U B 1 5 40""".stripMargin.toInputStream).map(_.toList).isFailure should be (true)
    }
  }

}
