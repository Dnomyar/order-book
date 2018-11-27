package order.book.domain.ports

import order.book.domain.TickSize
import order.book.domain.projections.OrderBookProjection

import scala.util.Try

trait OrderBookComputer {

  def compute(filename: String, tickSize: TickSize, bookDepth: Int): Try[List[OrderBookProjection]]

}
