package order.book.domain.commands

sealed trait OrderBookSide

object OrderBookSide {
  case object Bid extends OrderBookSide
  case object Ask extends OrderBookSide
}