package order.book.domain


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

  def minus(otherQuantity: Quantity): Order =
    copy(quantity = Quantity(Math.max(0, quantity.value - otherQuantity.value)))

  override def describe(tickSize: TickSize): (Double, Quantity) = (price.computePrice(tickSize), quantity)
}
