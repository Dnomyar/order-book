package order.book.domain


import order.book.domain.errors.DeletingTooMuchQuantityError

import scala.util.{Failure, Success, Try}


sealed trait OrderBookOrder {
  def describe(tickSize: TickSize): (Double, Quantity)
}

case object EmptyOrder extends OrderBookOrder {

  def withPrice(price: TickPrice, defaultQuantity: Quantity = Quantity(0)): Order =
    Order(price, defaultQuantity)

  override def describe(tickSize: TickSize): (Double, Quantity) = (0, Quantity(0))
}

case class Order(price: TickPrice, quantity: Quantity) extends OrderBookOrder {

  def replace(otherQuantity: Quantity): Order =
    copy(quantity = otherQuantity)

  def plus(otherQuantity: Quantity): Order =
    copy(quantity = Quantity(quantity.value + otherQuantity.value))

  def minus(otherQuantity: Quantity): Try[Order] =
    if(otherQuantity.value > quantity.value) Failure(new DeletingTooMuchQuantityError(price, quantity, otherQuantity))
    else Success(copy(quantity = Quantity(quantity.value - otherQuantity.value)))

  override def describe(tickSize: TickSize): (Double, Quantity) = (price.computePrice(tickSize), quantity)
}
