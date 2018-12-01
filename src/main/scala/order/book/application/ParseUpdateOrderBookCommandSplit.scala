package order.book.application

import order.book.application.errors.UnexpectedCharError
import order.book.domain.{Quantity, TickPrice}
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand

import scala.util.{Failure, Success, Try}

class ParseUpdateOrderBookCommandSplit extends ParseUpdateOrderBookCommand {

  override def parse(str: String): Try[UpdateOrderBookCommand] =
    str.split(" ") match {
      case Array(instructionRaw, sideRaw, priceLevelIndexRaw, priceRaw, quantityRaw) =>

        for{
          instruction <- isOneOf(instructionRaw)(List('N', 'U', 'D'))
          side <- isOneOf(sideRaw)(List('A', 'B'))
          priceLevelIndex <- Try(priceLevelIndexRaw.toInt)
          price <- Try(priceRaw.toInt)
          quantity <- Try(quantityRaw.toInt)
        } yield UpdateOrderBookCommand(
          instruction match {
            case 'N' => New
            case 'U' => Update
            case 'D' => Delete
          },
          side match {
            case 'A' => Ask
            case 'B' => Bid
          },
          priceLevelIndex,
          TickPrice(price),
          Quantity(quantity)
        )

    }


  def isOneOf(str: String)(expectedChars: List[Char]): Try[Char] = {
    val error = Failure(new UnexpectedCharError(expectedChars))
    if(str.length != 1) error
    else if(expectedChars.contains(str.head)) Success(str.head)
    else error
  }
}
