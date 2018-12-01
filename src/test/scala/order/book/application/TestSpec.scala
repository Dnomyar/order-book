package order.book.application

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.commands.{OrderBookInstruction, OrderBookSide, UpdateOrderBookCommand}
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import org.scalatest.{Matchers, WordSpec}

class TestSpec extends WordSpec with Matchers {


  case class State[U,V](run: U => (U, V)) {

    def map[W](f: V => W): State[U, W] = State(run(_) match {
      case (a, s) => (a, f(s))
    })

    def flatMap[W](f: V => State[U, W]): State[U, W] = State(d => run(d) match {
        case (a, s) => f(s).run(a)
    })

  }



  def parseChar(str: String)(p: Char => Boolean): (String, Option[Char]) = str.headOption match {
    case Some(head) if p(head) => (str.tail, Some(head))
    case _ => (str, None)
  }


  val whitespaceParser = State[String, Char](s => (s.tail, s.head))

  val numberParser = State[String, Int](s => s.span(_.isDigit) match {
    case (number, tail) => (tail, number.toInt)
  })

  val instructionParser = State[String, OrderBookInstruction](s => s.headOption match {
    case Some(char) if char == 'N' => (s.tail, New)
    case Some(char) if char == 'U' => (s.tail, Update)
    case Some(char) if char == 'D' => (s.tail, Delete)
  })

  val sideParser = State[String, OrderBookSide](s => s.headOption match {
    case Some(char) if char == 'A' => (s.tail, Ask)
    case Some(char) if char == 'B' => (s.tail, Bid)
  })


  "It" should {
    "be possible to parse a numberParser" in {
      numberParser.run("11")._2 should be (11)
    }
    "be possible to parse a whitespaceParser" in {
      whitespaceParser.run(" azd")._2 should be (' ')
    }
    "be possible to parse two numbers separated by a whitespaceParser" in {
     (
       for{
         n1 <- numberParser
         _ <- whitespaceParser
         n2 <- numberParser
       } yield (n1, n2)
     ).run("11 23")._2 should be ((11, 23))
    }

    "be possible to parse a line `N B 2 4 40`" in {
     (
       for{
         instruction <- instructionParser
         _ <- whitespaceParser
         side <- sideParser
         _ <- whitespaceParser
         priceLevelIndex <- numberParser
         _ <- whitespaceParser
         price <- numberParser
         _ <- whitespaceParser
         quantity <- numberParser
       } yield UpdateOrderBookCommand(instruction, side, priceLevelIndex, TickPrice(price), Quantity(quantity))
     ).run("N B 2 4 40")._2 should be (UpdateOrderBookCommand(
       New, Bid, 2, TickPrice(4), Quantity(40)
     ))
    }
  }

}
