package order.book.application

import order.book.domain.OrderBookComputerImplementation

object ConsoleEndPoint {

  def main(args: Array[String]): Unit = {
    // should use DI instead
    new ConsoleAdapter(new OrderBookComputerImplementation, new UpdateOrderBookCommandParser)
      .compute(args)


  }

}
