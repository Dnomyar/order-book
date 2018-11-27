package order.book.application


import order.book.domain.TickSize
import order.book.domain.ports.OrderBookComputer
import order.book.domain.projections.OrderBookProjection

import scala.util.{Failure, Success, Try}

class ConsoleAdapter(orderBookComputer: OrderBookComputer, updateOrderBookCommandParser: UpdateOrderBookCommandParser) {

  def compute(args: Array[String]): Unit = {
    (
      for {
        (filename, tickSize, bookDepth) <- parseArguments(args)
        updateOrderBookCommand          <- updateOrderBookCommandParser.parseFile(filename)
        orderBookProjections            <- orderBookComputer.compute(updateOrderBookCommand, tickSize, bookDepth)
      } yield orderBookProjections
    ) match {
      case Success(orderBookProjections) =>
        println(
          orderBookProjections
          .map(p => s"${p.bidPrice},${p.bidQuantity.value},${p.askPrice},${p.askQuantity.value}")
          .mkString("\n")
        )

      case Failure(exception) =>
        println(exception)
        exception.printStackTrace
    }
  }

  def parseArguments(args: Array[String]): Try[(String, TickSize, Int)] = for{
    tickSize <- Try(args(1).toFloat)
    bookDepth <- Try(args(2).toInt)
  } yield (args(0), TickSize(tickSize), bookDepth)


  def orderBookProjectionToString(p: OrderBookProjection): String =
    s"${p.bidPrice},${p.bidQuantity.value},${p.askPrice},${p.askQuantity.value}"
}
