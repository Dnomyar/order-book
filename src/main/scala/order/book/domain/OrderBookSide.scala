package order.book.domain

import order.book.domain.change.request.OrderBookInstruction
import order.book.domain.change.request.OrderBookInstruction.{Delete, New, Update}

case class OrderBookSide private[OrderBookSide] (orders: Vector[OrderBookOrder]) {

  def applyOrderChange(instruction: OrderBookInstruction,
                       priceLevelIndex: Int,
                       price: TickPrice,
                       quantity: Quantity): OrderBookSide = {

    val updateOrdersIndexed = updateOrders(priceLevelIndex)(_)

    instruction match {
      case New =>
        updateOrdersIndexed{
          case order: Order => order.plus(quantity)
          case EmptyOrder => EmptyOrder.withPrice(price).plus(quantity)
        }

      case Update =>
        updateOrdersIndexed{
          case order: Order => order.replace(quantity)
          case EmptyOrder => EmptyOrder.withPrice(price).plus(quantity) // TODO should not happen
        }

      case Delete =>
        updateOrdersIndexed{
          case order: Order => order.minus(quantity) // TODO delete more than existing
          case EmptyOrder => EmptyOrder // TODO delete an empty order
        }
    }
  }


  private def updateOrders(priceLevelIndex: Int)(updateOrder: OrderBookOrder => OrderBookOrder): OrderBookSide = {
    val vectorIndex = priceLevelIndex - 1

    copy(orders =
      orders.updated(vectorIndex, // todo index out of bound exception
        updateOrder(orders(vectorIndex))
      )
    )
  }
}

object OrderBookSide {
  def apply(bookDepth: Int): OrderBookSide = new OrderBookSide(Vector.fill(bookDepth)(EmptyOrder))
}