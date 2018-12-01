package order.book.domain.ports

import akka.stream.IOResult
import akka.stream.scaladsl.Source
import order.book.domain.TickSize
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.projections.OrderBookProjection

import scala.concurrent.Future
import scala.util.Try

trait OrderBookComputer {

  def compute(updateOrderBookCommands: Iterator[UpdateOrderBookCommand], tickSize: TickSize, bookDepth: Int): Try[List[OrderBookProjection]]

  def computeAkkaStream(updateOrderBookCommands: Source[UpdateOrderBookCommand, Future[IOResult]], tickSize: TickSize, bookDepth: Int): Source[OrderBookProjection, Future[IOResult]]

}
