package order.book.domain

import org.scalatest.{Matchers, WordSpec}

import scala.util.{Failure, Success}

class OrderBookOrderSpec extends WordSpec with Matchers {

  "It" should {
    "be possible to add a quantity to an existing order" in {
      Order(TickPrice(1), Quantity(10))
        .plus(Quantity(20)) should be (Order(
        TickPrice(1), Quantity(30)
      ))
    }

    "be possible to remove a quantity to an existing order (|20 - 10| = 10)" in {
      Order(TickPrice(1), Quantity(20))
        .minus(Quantity(10)) should be (Success(Order(
        TickPrice(1), Quantity(10)
      )))
    }

    "be possible to remove a quantity to an existing order (|10 - 10| = 0)" in {
      Order(TickPrice(1), Quantity(10))
        .minus(Quantity(10)) should be (Success(Order(
        TickPrice(1), Quantity(0)
      )))
    }

    "be possible to remove a quantity to an existing order (|10 - 20| = 0)" in {
      Order(TickPrice(1), Quantity(10))
        .minus(Quantity(20)).isFailure should be (true)
    }

    "be possible to replace a quantity to an existing order" in {
      Order(TickPrice(1), Quantity(10))
        .replace(Quantity(20)) should be (Order(
        TickPrice(1), Quantity(20)
      ))
    }

    "be possible to create a Order from an EmptyOrder" in {
      EmptyOrder.withPrice(TickPrice(5)) should be (Order(TickPrice(5), Quantity(0)))
    }

    "be possible to describe an EmptyOrder" in {
      EmptyOrder.describe(TickSize(10)) should be ((0, Quantity(0)))
    }

    "be possible to describe an Order" in {
      Order(TickPrice(1), Quantity(10)).describe(TickSize(10)) should be ((10, Quantity(10)))
    }
  }

}
