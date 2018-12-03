package order.book.domain

import order.book.domain.commands.OrderBookInstruction
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.datastructure.AVLIndexedTree

import scala.util.{Success, Try}

class OrderBookSide private[OrderBookSide] (orders: AVLIndexedTree[OrderBookOrder]) {

  def applyOrderChange(instruction: OrderBookInstruction,
                       priceLevelIndex: Int,
                       price: TickPrice,
                       quantity: Quantity): Try[OrderBookSide] = {
    val zeroStartingIndex = priceLevelIndex - 1
    Success(instruction match {
      case New => new OrderBookSide(orders.insert(zeroStartingIndex, Order(price, quantity)))
      case Update => new OrderBookSide(orders.updated(zeroStartingIndex, Order(price, quantity)))
      case Delete => new OrderBookSide(orders.delete(zeroStartingIndex))
    })
  }

  def getOrders: List[OrderBookOrder] = orders.toList

}

object OrderBookSide {
  def empty: OrderBookSide = new OrderBookSide(AVLIndexedTree.empty)
}