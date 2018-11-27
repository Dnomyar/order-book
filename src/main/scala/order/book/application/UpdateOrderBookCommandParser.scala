package order.book.application

import java.io.{File, FileInputStream, InputStream}

import order.book.application.errors.UnexpectedCharError
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.UpdateOrderBookCommand
import order.book.domain.{Quantity, TickPrice}

import scala.io.Source
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}


class UpdateOrderBookCommandParser {

  def parseFile(filename: String): Try[Iterator[UpdateOrderBookCommand]] =
    for{
      inputStream <- Try(new FileInputStream(new File(filename)))
      orders <- parseInputStream(inputStream)
    } yield orders


  // TODO not the most elegant way
  def parseInputStream(inputStream: InputStream): Try[Iterator[UpdateOrderBookCommand]] = Try{
    Source.fromInputStream(inputStream).getLines()
      .map(readLine)
      .collect{
        case Success(value) => value
        case Failure(exception) => throw exception
      }
  }


  private def readLine(line: String): Try[UpdateOrderBookCommand] = {

    def isOneOf(str: String)(expectedChars: List[Char]): Try[Char] = {
      val error = Failure(new UnexpectedCharError(expectedChars))
      if(str.length != 1) error
      else if(expectedChars.contains(str.head)) Success(str.head)
      else error
    }

    line.split(" ") match {
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

  }

}
