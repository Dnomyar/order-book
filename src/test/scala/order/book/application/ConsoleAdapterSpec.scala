package order.book.application


import java.io.ByteArrayOutputStream

import order.book.domain.projections.OrderBookProjection
import order.book.domain.{OrderBookComputerImplementation, Quantity, TickSize}
import org.scalatest.{Matchers, WordSpec}

import scala.util.Success

class ConsoleAdapterSpec extends WordSpec with Matchers {

  val consoleAdapter = new ConsoleAdapter(new OrderBookComputerImplementation, new UpdateOrderBookCommandParser(new ParseUpdateOrderBookCommandStateMonad))

  "Program arguments" should {
    "be parsable" in {
      consoleAdapter.parseArguments(Array("updates.txt", "10.0", "2")) should be (Success((
        "updates.txt",
        TickSize(10),
        2
      )))
    }
  }

  "It" should {

    "be possible transform an OrderBookProjection to string" in {
      consoleAdapter.orderBookProjectionToString(
        OrderBookProjection(50, Quantity(40), 60, Quantity(10))
      ) should be ("50.0,40,60.0,10")
    }

    "be possible to read a file, compute the book and print the results" in {
      val stream = new ByteArrayOutputStream()
      Console.withOut(stream){
        consoleAdapter.compute(Array("updates.txt", "10.0", "2"))
      }

      stream.toString.trim should be ("""50.0,40,60.0,10
                                   |40.0,40,70.0,20""".stripMargin)
    }
  }

}
