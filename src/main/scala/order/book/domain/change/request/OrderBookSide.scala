package order.book.domain.change.request

sealed trait OrderBookSide

object OrderBookSide {
  case object Bid extends OrderBookSide
  case object Ask extends OrderBookSide
}