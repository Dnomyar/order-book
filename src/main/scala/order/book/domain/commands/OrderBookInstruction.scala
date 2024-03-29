package order.book.domain.commands

sealed trait OrderBookInstruction

object OrderBookInstruction {
  case object New extends OrderBookInstruction
  case object Update extends OrderBookInstruction
  case object Delete extends OrderBookInstruction
}
