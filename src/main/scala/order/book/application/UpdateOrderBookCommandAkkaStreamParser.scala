package order.book.application

import java.nio.file.Paths

import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString
import order.book.domain.commands.UpdateOrderBookCommand

import scala.concurrent.Future
import scala.util.{Success, Try}

class UpdateOrderBookCommandAkkaStreamParser(parseUpdateOrderBookCommand: ParseUpdateOrderBookCommand) {

  def parseFile(filename: String): Source[UpdateOrderBookCommand, Future[IOResult]] =
    FileIO.fromPath(Paths.get(filename))
      .via(Framing.delimiter(ByteString("\n"), 256, allowTruncation = true).map(_.utf8String))
      .mapAsync(4)(e => Future.successful(parseUpdateOrderBookCommand.parse(e)))
      .collect{
        case Success(value) => value
      }


}
