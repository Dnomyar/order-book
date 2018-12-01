package order.book.application

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import order.book.domain.TickSize
import order.book.domain.ports.OrderBookComputer
import order.book.domain.projections.OrderBookProjection

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

import order.book.application.AkkaObjects._


class ConsoleAdapterAkkaStream(orderBookComputer: OrderBookComputer,
                               updateOrderBookCommandParser: UpdateOrderBookCommandAkkaStreamParser) {


  def compute(args: Array[String]): Unit = {

    parseArguments(args) match {
      case Success((filename, tickSize, bookDepth)) =>

        val parsedSource = updateOrderBookCommandParser.parseFile(filename)

        orderBookComputer.computeAkkaStream(parsedSource, tickSize, bookDepth)
          .map(orderBookProjectionToString)
          .to(Sink.foreach(println))
          .run
          .map(_ => system.terminate())

      case Failure(exception) =>
        exception.printStackTrace()

    }

  }

  def parseArguments(args: Array[String]): Try[(String, TickSize, Int)] = for{
    tickSize <- Try(args(1).toFloat)
    bookDepth <- Try(args(2).toInt)
  } yield (args(0), TickSize(tickSize), bookDepth)


  def orderBookProjectionToString(p: OrderBookProjection): String =
    s"${p.bidPrice},${p.bidQuantity.value},${p.askPrice},${p.askQuantity.value}"
}
