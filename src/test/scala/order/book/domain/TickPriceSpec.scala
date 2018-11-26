package order.book.domain

import org.scalatest.{Matchers, WordSpec}

class TickPriceSpec extends WordSpec with Matchers {

  "It" should {
    "possible to compute the price from the tick price and the tick size" in {
      TickPrice(10).computePrice(TickSize(5)) should be (50d)
    }
  }

}
