package order.book.application

import java.io.{File, FileInputStream, InputStream}

import akka.actor.ActorRef
import order.book.application.ParseActor.{Finished, ParseLine}
import order.book.domain.commands.UpdateOrderBookCommand

import scala.io.Source
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}


class UpdateOrderBookCommandParser(parseUpdateOrderBookCommand: ParseUpdateOrderBookCommand) {

  def parseFile(filename: String, parserActor: ActorRef): Unit = {
    for{
      inputStream <- Try(new FileInputStream(new File(filename)))
    } yield parseInputStream(inputStream, parserActor)
    parserActor ! Finished
  }


//  // TODO not the most elegant way
//  def parseInputStream(inputStream: InputStream): Try[Iterator[UpdateOrderBookCommand]] = Try{
//    Source.fromInputStream(inputStream).getLines()
//      .map(parseUpdateOrderBookCommand.parse)
//      .collect{
//        case Success(value) => value
//        case Failure(exception) => throw exception
//      }
//  }




  // TODO not the most elegant way
  def parseInputStream(inputStream: InputStream,  parserActor: ActorRef): Unit = Try{
    Source.fromInputStream(inputStream).getLines()
      .foreach(line => parserActor ! ParseLine(line))
  }

}
