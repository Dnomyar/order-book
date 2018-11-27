package order.book.domain

import order.book.domain.change.request.OrderBookInstruction
import order.book.domain.change.request.OrderBookInstruction.{Delete, New, Update}
import order.book.domain.errors.{CannotDeleteEmptyOrderError, UpdatingNonExistingValueError}

import scala.util.{Failure, Success, Try}

case class OrderBookSide private[OrderBookSide] (orders: Vector[OrderBookOrder]) {

  def applyOrderChange(instruction: OrderBookInstruction,
                       priceLevelIndex: Int,
                       price: TickPrice,
                       quantity: Quantity): Try[OrderBookSide] = {

    val updateOrdersIndexed = updateOrders(priceLevelIndex)(_)

    instruction match {
      case New =>
        updateOrdersIndexed{
          case order: Order => Success(order.plus(quantity))
          case EmptyOrder => Success(EmptyOrder.withPrice(price).plus(quantity))
        }

      case Update =>
        updateOrdersIndexed{
          case order: Order => Success(order.replace(quantity))
          case EmptyOrder => Failure(new UpdatingNonExistingValueError(priceLevelIndex, price, quantity))
        }

      case Delete =>
        updateOrdersIndexed{
          case order: Order => order.minus(quantity)
          case EmptyOrder => Failure(new CannotDeleteEmptyOrderError(priceLevelIndex, price, quantity))
        }
    }
  }


  private def updateOrders(priceLevelIndex: Int)(updateOrder: OrderBookOrder => Try[OrderBookOrder]): Try[OrderBookSide] = {
    val vectorIndex = priceLevelIndex - 1

    for{
      orderToUpdate <- Try(orders(vectorIndex))
      newOrder <- updateOrder(orderToUpdate)
      ordersUpdated <- Try(orders.updated(vectorIndex, newOrder))
    } yield copy(orders = ordersUpdated)
  }
}

object OrderBookSide {
  def apply(bookDepth: Int): OrderBookSide = new OrderBookSide(Vector.fill(bookDepth)(EmptyOrder))
}