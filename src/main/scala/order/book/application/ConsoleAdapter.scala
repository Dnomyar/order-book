package order.book.application

import java.io.{File, FileInputStream}

import order.book.domain.ports.OrderBookComputer
import order.book.domain.{OrderBookComputerImplementation, TickSize}
import order.book.infrastructure.OrderBookChangeRequestParserAdapter

import scala.util.{Failure, Success, Try}

class ConsoleAdapter(orderBookComputer: OrderBookComputer) {

  def compute(args: Array[String]): Unit = {
    (
      for {
        (filename, tickSize, bookDepth) <- parseArguments(args)
        orderBookProjection             <- orderBookComputer.compute(filename, tickSize, bookDepth)
      } yield orderBookProjection
    ) match {
      case Success(orderBookProjection) => println(orderBookProjection)
      case Failure(exception) => println(exception)
    }
  }

  def parseArguments(args: Array[String]): Try[(String, TickSize, Int)] = for{
    tickSize <- Try(args(1).toFloat)
    bookDepth <- Try(args(2).toInt)
  } yield (args(0), TickSize(tickSize), bookDepth)

}
