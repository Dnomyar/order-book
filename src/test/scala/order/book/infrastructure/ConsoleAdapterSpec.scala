package order.book.infrastructure

import order.book.application.ConsoleAdapter
import order.book.domain.TickSize
import org.scalatest.{Matchers, WordSpec}

import scala.util.Success

class ConsoleAdapterSpec extends WordSpec with Matchers {

  "Program arguments" should {
    "be parsable" in {
      ConsoleAdapter.parseArguments(Array("updates.txt", "10.0", "2")) should be (Success((
        "updates.txt",
        TickSize(10),
        2
      )))
    }
  }

}
