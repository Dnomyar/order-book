package order.book.application

import order.book.domain.OrderBookComputerImplementation
import order.book.infrastructure.OrderBookChangeRequestParserAdapter

object ConsoleEndPoint {

  def main(args: Array[String]): Unit = {
    // should use DI instead
    new ConsoleAdapter(new OrderBookComputerImplementation(new OrderBookChangeRequestParserAdapter)).compute(args)
  }

}
