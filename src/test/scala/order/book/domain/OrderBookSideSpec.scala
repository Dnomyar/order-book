package order.book.domain

import order.book.domain.change.request.OrderBookInstruction.{Delete, New, Update}
import org.scalatest.{Matchers, WordSpec}

class OrderBookSideSpec extends WordSpec with Matchers {

  "It" should {
    "be possible to add an order in a side of the book" in {
      OrderBookSide(1).applyOrderChange(
        New,
        1,
        TickPrice(5),
        Quantity(30)
      ).get.orders should be (Vector(
        Order(TickPrice(5), Quantity(30))
      ))
    }

    "be possible to update an order in a side of the book" in {
      OrderBookSide(1)
        .applyOrderChange(
          New,
          1,
          TickPrice(5),
          Quantity(30)
        )
        .get.applyOrderChange(
          Update,
          1,
          TickPrice(5),
          Quantity(40)
        ).get.orders should be (Vector(
          Order(TickPrice(5), Quantity(40))
        ))
    }

    "be possible to delete an order in a side of the book" in {
      OrderBookSide(1)
        .applyOrderChange(
          New,
          1,
          TickPrice(5),
          Quantity(30)
        )
        .get.applyOrderChange(
          Delete,
          1,
          TickPrice(5),
          Quantity(10)
        ).get.orders should be (Vector(
          Order(TickPrice(5), Quantity(20))
        ))
    }

    "be possible to do several actions and get the excepted result" in {
      OrderBookSide(2)
        .applyOrderChange(
          New,
          1,
          TickPrice(5),
          Quantity(30)
        )
        .get.applyOrderChange(
          New,
          2,
          TickPrice(4),
          Quantity(40)
        )
        .get.applyOrderChange(
          Update,
          1,
          TickPrice(5),
          Quantity(40)
        ).get.orders should be (Vector(
          Order(TickPrice(5), Quantity(40)),
          Order(TickPrice(4), Quantity(40))
        ))
    }
  }

}
