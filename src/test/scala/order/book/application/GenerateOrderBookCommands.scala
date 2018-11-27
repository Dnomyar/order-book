package order.book.application

import java.io.{File, FileOutputStream, OutputStreamWriter, PrintWriter}
import java.nio.file.Files

import order.book.domain.{Quantity, TickPrice}
import order.book.domain.commands.OrderBookInstruction._
import order.book.domain.commands.OrderBookSide.{Ask, Bid}
import order.book.domain.commands.{OrderBookInstruction, UpdateOrderBookCommand}

import scala.util.Random


/**
  * WARNING : this is trash code
  */
object GenerateOrderBookCommands {

  val NUMBER_OF_UPDATES = 10000000
  val BOOK_DEPTH = 100
  val AVG_PRICE = 100
  val MAX_QUANTITY = 1000

  val random = new Random()

  def main(args: Array[String]): Unit = {

    val writer = new OutputStreamWriter(new FileOutputStream("lot-of-updates.txt"), "UTF-8")

    val originalBids: Vector[(Int, Int, Int)] = Stream
      .iterate(AVG_PRICE)(_ - 1)
      .take(BOOK_DEPTH)
      .map(idx => (idx, Math.abs(idx), 0))
      .toVector

    val originalAsks: Vector[(Int, Int, Int)] = Stream
      .iterate(AVG_PRICE)(_ + 1)
      .take(BOOK_DEPTH)
      .map(idx => (idx, idx, 0))
      .toVector

    (1 to NUMBER_OF_UPDATES).toIterator
      .foldLeft((originalBids, originalAsks)){
        case ((bids, asks), _) =>
          val (acc, cmd) = generateUpdateOrderBookCommand(bids, asks)
          printUpdateOrderBookCommand(writer)(cmd)
          acc
      }

    writer.flush()
    writer.close()


  }


  def generateUpdateOrderBookCommand(bids: Vector[(Int, Int, Int)],
                                     asks: Vector[(Int, Int, Int)]): ((Vector[(Int, Int, Int)], Vector[(Int, Int, Int)]), UpdateOrderBookCommand) = {

    val idx = random.nextInt(BOOK_DEPTH - 1) + 1

    val quantity = random.nextInt(MAX_QUANTITY)



    val side = choseOne(Vector(Ask, Bid))


    val instruction = (choseOne(Vector(New, Update, Delete)), side) match {
      case (Update, Ask) if asks(idx)._3 == 0 => New
      case (Update, Bid) if bids(idx)._3 == 0 => New
      case (Delete, Ask) if asks(idx)._3 == 0 => New
      case (Delete, Bid) if bids(idx)._3 == 0 => New
      case (i, _) => i
    }

    val price = side match {
      case Ask => asks(idx)._2
      case Bid => bids(idx)._2
    }


    val (realQuantity, newBids, newAsks) = (instruction, side) match {
      case (New, Ask) => (quantity, bids, asks.updated(idx, asks(idx).copy(_3 = quantity + asks(idx)._3)))
      case (New, Bid) => (quantity, bids.updated(idx, bids(idx).copy(_3 = quantity + bids(idx)._3)), asks)
      case (Update, Ask) => (quantity, bids, asks.updated(idx, asks(idx).copy(_3 = quantity)))
      case (Update, Bid) => (quantity, bids.updated(idx, bids(idx).copy(_3 = quantity)), asks)
      case (Delete, Ask) =>
        val quantityToDelete = random.nextInt(asks(idx)._3)
        (quantityToDelete, bids, asks.updated(idx, asks(idx).copy(_3 = asks(idx)._3 - quantityToDelete)))
      case (Delete, Bid) =>
        val quantityToDelete = random.nextInt(bids(idx)._3)
        (quantityToDelete, bids.updated(idx, bids(idx).copy(_3 = bids(idx)._3 - quantityToDelete)), asks)
    }


    (
      (newBids, newAsks),
      UpdateOrderBookCommand(
        instruction,
        side,
        idx,
        TickPrice(price),
        Quantity(realQuantity)
      )
    )
  }

  def choseOne[T](xs: Vector[T]): T = xs(random.nextInt(xs.length))


  def printUpdateOrderBookCommand(writer: OutputStreamWriter)(command: UpdateOrderBookCommand): Unit = {

    command.instruction match {
      case OrderBookInstruction.New => writer.append('N')
      case OrderBookInstruction.Update => writer.append('U')
      case OrderBookInstruction.Delete => writer.append('D')
    }

    writer.append(' ')

    command.side match {
      case Ask => writer.append('A')
      case Bid => writer.append('B')
    }

    writer.append(' ')

    writer.append(command.priceLevelIndex.toString)

    writer.append(' ')

    writer.append(command.price.value.toString)

    writer.append(' ')

    writer.append(command.quantity.value.toString)

    writer.append('\n')

  }



}
