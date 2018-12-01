package order.book.domain

import order.book.domain.commands.OrderBookInstruction
import order.book.domain.commands.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.datastructure.BinaryTree
import order.book.domain.errors.{CannotDeleteEmptyOrderError, UpdatingNonExistingValueError}

import scala.util.{Failure, Success, Try}

case class OrderBookSide private[OrderBookSide] (orders: BinaryTree[OrderBookOrder]) {

  // O(1)
  def applyOrderChange(instruction: OrderBookInstruction,
                       priceLevelIndex: Int,
                       price: TickPrice,
                       quantity: Quantity): Try[OrderBookSide] = {

    //val updateOrdersIndexed = updateOrders(priceLevelIndex)(_)


    instruction match {
      case New =>
        import scala.collection.Searching._
        IndexedSeq(1,2,3,4,5).search(4)

        OrderBookSide(orders.insert(Order(price, quantity)))

//        updateOrdersIndexed{
//          case order: Order => Success(order.plus(quantity))
//          case EmptyOrder => Success(EmptyOrder.withPrice(price).plus(quantity))
//        }

      case Update =>
//        updateOrdersIndexed{
//          case order: Order => Success(order.replace(quantity))
//          case EmptyOrder => Failure(new UpdatingNonExistingValueError(priceLevelIndex, price, quantity))
//        }

      case Delete =>
        OrderBookSide(orders.delete(Order(price, quantity)))
//        updateOrdersIndexed{
//          case order: Order => order.minus(quantity)
//          case EmptyOrder => Failure(new CannotDeleteEmptyOrderError(priceLevelIndex, price, quantity))
//        }
    }
  }


  // O(updateOrders) = O(updateOrder)
//  private def updateOrders(priceLevelIndex: Int)(updateOrder: OrderBookOrder => Try[OrderBookOrder]): Try[OrderBookSide] = {
//    val vectorIndex = priceLevelIndex - 1
//
//    for{
//      orderToUpdate <- Try(orders(vectorIndex)) // O(1)
//      newOrder <- updateOrder(orderToUpdate)
//      ordersUpdated <- Try(orders.updated(vectorIndex, newOrder)) // O(1)
//    } yield copy(orders = ordersUpdated)
//  }
}

object OrderBookSide {
  def apply(bookDepth: Int): OrderBookSide = new OrderBookSide(Vector.fill(bookDepth)(EmptyOrder))
}