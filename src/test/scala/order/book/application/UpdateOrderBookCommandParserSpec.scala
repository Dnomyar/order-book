package order.book.application

import order.book.domain.commands.OrderBookInstruction.{New, Update}
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.{Quantity, TickPrice}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Success

class UpdateOrderBookCommandParserSpec extends WordSpec with Matchers {

  val parser = new UpdateOrderBookCommandParser(new ParseUpdateOrderBookCommandStateMonad)


  "It" should {
    "be possible to parseFile a file if the file is ok" in {
      parser.parseInputStream(
        """N B 1 5 30
          |N B 2 4 40
          |N A 1 6 10
          |N A 2 7 10
          |U A 2 7 20
          |U B 1 5 40""".stripMargin.toInputStream).map(_.toList) should be (Success(List(
        UpdateOrderBookCommand(New, Bid, 1, TickPrice(5), Quantity(30)),
        UpdateOrderBookCommand(New, Bid, 2, TickPrice(4), Quantity(40)),
        UpdateOrderBookCommand(New, Ask, 1, TickPrice(6), Quantity(10)),
        UpdateOrderBookCommand(New, Ask, 2, TickPrice(7), Quantity(10)),
        UpdateOrderBookCommand(Update, Ask, 2, TickPrice(7), Quantity(20)),
        UpdateOrderBookCommand(Update, Bid, 1, TickPrice(5), Quantity(40))
      )))
    }

    "be possible to parseFile a file with errors" in {
      parser.parseInputStream(
        """N B 1 5 30
          |N B 2 4 40
          |N A 1 6 10
          |N A 2 7 10
          |U  2 7 20
          |U B 1 5 40""".stripMargin.toInputStream).map(_.toList).isFailure should be (true)
    }
  }

}
