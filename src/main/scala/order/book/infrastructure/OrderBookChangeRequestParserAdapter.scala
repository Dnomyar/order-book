package order.book.infrastructure

import java.io.{File, FileInputStream, InputStream}

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.change.request.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.change.request.OrderBookSide.{Ask, Bid}
import order.book.domain.ports.OrderBookChangeRequestFetcher

import scala.io.Source
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}


class OrderBookChangeRequestParserAdapter extends OrderBookChangeRequestFetcher {

  override def fetch(filename: String): Try[Iterator[OrderBookChangeRequest]] =
    for{
      inputStream <- Try(new FileInputStream(new File(filename)))
      orders <- parseFile(inputStream)
    } yield orders


  // TODO not the most elegant way
  def parseFile(inputStream: InputStream): Try[Iterator[OrderBookChangeRequest]] = Try{
    Source.fromInputStream(inputStream).getLines()
      .map(readLine)
      .collect{
        case Success(value) => value
        case Failure(exception) => throw exception
      }
  }


  private def readLine(line: String): Try[OrderBookChangeRequest] = {

    def isOneOf(str: String)(expectedChars: List[Char]): Try[Char] = {
      val error = Failure(new Exception(s"Expected one of $expectedChars"))
      if(str.length != 1) error
      else if(expectedChars.contains(str.head)) Success(str.head)
      else error
    }

    line.trim.split(" ").map(_.trim) match {
      case Array(instructionRaw, sideRaw, priceLevelIndexRaw, priceRaw, quantityRaw) =>

        for{
          instruction <- isOneOf(instructionRaw)(List('N', 'U', 'D'))
          side <- isOneOf(sideRaw)(List('A', 'B'))
          priceLevelIndex <- Try(priceLevelIndexRaw.toInt)
          price <- Try(priceRaw.toInt)
          quantity <- Try(quantityRaw.toInt)
        } yield OrderBookChangeRequest(
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
