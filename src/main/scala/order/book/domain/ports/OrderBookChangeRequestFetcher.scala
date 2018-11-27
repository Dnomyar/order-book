package order.book.domain.ports

import order.book.domain.change.request.OrderBookChangeRequest

import scala.util.Try

trait OrderBookChangeRequestFetcher {

  def fetch(filename: String): Try[Iterator[OrderBookChangeRequest]]

}
