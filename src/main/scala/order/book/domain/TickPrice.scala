package order.book.domain

case class TickPrice(value: Int) extends AnyVal {

  def computePrice(tickSize: TickSize): Double = tickSize.value.toDouble * value.toDouble

}
