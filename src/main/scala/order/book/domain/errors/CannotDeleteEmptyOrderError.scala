package order.book.domain.errors

import order.book.domain.{Quantity, TickPrice}

class CannotDeleteEmptyOrderError(priceLevelIndex: Int, price: TickPrice, quantity: Quantity)
  extends Exception(s"Cannot delete empty order [priceLevelIndex:$priceLevelIndex][tickPrice:$price][quantity:$quantity]")