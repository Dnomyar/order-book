package order.book.application

import order.book.domain.OrderBookComputerImplementation

object ConsoleEndPoint {

  def main(args: Array[String]): Unit = {

    println("ConsoleEndPoint")

    val parser: ParseUpdateOrderBookCommand =
      new ParseUpdateOrderBookCommandStateMonad
//      new ParseUpdateOrderBookCommandSplit


    // should use DI instead


    //new ConsoleAdapterAkkaStream(new OrderBookComputerImplementation, new UpdateOrderBookCommandAkkaStreamParser(parser))
    new ConsoleAdapter(new OrderBookComputerImplementation, new UpdateOrderBookCommandParser(parser))
      .compute(args)


  }

}
