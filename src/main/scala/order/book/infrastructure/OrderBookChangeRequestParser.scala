package order.book.infrastructure

import java.io.{File, FileInputStream, InputStream}

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.change.request.OrderBookChangeRequest
import order.book.domain.change.request.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.change.request.OrderBookSide.{Ask, Bid}

import scala.io.Source
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

case class State[S, +A](run: S => (A, S)) {
  def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
    val (a, s1) = run(s)
    f(a).run(s1)
  })

  def map[B](f: A => B): State[S, B] =
    flatMap(a => unit(f(a)))


  def unit[B](b: B): State[S, B] = State(s => (b, s))
}

object State {
  def get[S]: State[S, S] = State(s => (s, s))

  def set[S](s: S): State[S, Unit] = State(_ => ((), s))
}

class OrderBookChangeRequestParser {

  type Parser[T] = State[List[String], T]

  def parseFile(fileName: String): Try[Iterator[OrderBookChangeRequest]] = {
    for{
      inputStream <- Try(new FileInputStream(new File(fileName)))
      res <- parseFile(inputStream)
    } yield res
  }


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
      val error = util.Failure(new Exception(s"Expected one of $expectedChars"))
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

//  private def matchChar(p: Char => Boolean): Parser[Char] = {
//
//    State.get[String].map{
//      case "" =>
//    }
////    for {
////      str <- State.get[String]
////
////      //    str.headOption match {
////      //      case None => Left(List("Empty string"))
////      //      case Some(c) if p(c) => Right(c)
////      //      case Some(c) => Left(List(s"Unexpected char $c"))
////      //    }
////    }
//
//      ???
//  }

}
