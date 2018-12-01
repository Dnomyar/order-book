package order.book.application

import order.book.domain.commands.UpdateOrderBookCommand

import scala.util.Try

trait ParseUpdateOrderBookCommand {

  def parse(str: String): Try[UpdateOrderBookCommand]

}
