package order.book.domain.errors

import order.book.domain.{Quantity, TickPrice}

class DeletingTooMuchQuantityError(price: TickPrice, currentQuantity: Quantity, quantityToDelete: Quantity)
  extends Exception(s"Cannot delete empty order [tickPrice:$price][currentQuantity:$currentQuantity][quantityToDelete:$quantityToDelete]")
