package order.book.application

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.{OrderBookInstruction, OrderBookSide, UpdateOrderBookCommand}

import scala.util.Try

class ParseUpdateOrderBookCommandStateMonad extends ParseUpdateOrderBookCommand {


  override def parse(str: String): Try[UpdateOrderBookCommand] =
    Try(parser.run(str)._2)


  val whitespaceParser: State[String, Char] = State[String, Char](s => (s.tail, s.head))

  val numberParser: State[String, Int] = State[String, Int](s => s.span(_.isDigit) match {
    case (number, tail) => (tail, number.toInt)
  })

  val instructionParser: State[String, OrderBookInstruction] = State[String, OrderBookInstruction](s => s.headOption match {
    case Some(char) if char == 'N' => (s.tail, New)
    case Some(char) if char == 'U' => (s.tail, Update)
    case Some(char) if char == 'D' => (s.tail, Delete)
  })

  val sideParser: State[String, OrderBookSide] = State[String, OrderBookSide](s => s.headOption match {
    case Some(char) if char == 'A' => (s.tail, Ask)
    case Some(char) if char == 'B' => (s.tail, Bid)
  })

  val parser: State[String, UpdateOrderBookCommand] = for{
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


}


case class State[U,V](run: U => (U, V)) {

  def map[W](f: V => W): State[U, W] = State(run(_) match {
    case (a, s) => (a, f(s))
  })

  def flatMap[W](f: V => State[U, W]): State[U, W] = State(d => run(d) match {
    case (a, s) => f(s).run(a)
  })

}