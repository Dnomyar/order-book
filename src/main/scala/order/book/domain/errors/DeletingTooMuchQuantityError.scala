package order.book.domain.errors

import order.book.domain.{Quantity, TickPrice}

class DeletingTooMuchQuantityError(price: TickPrice, currentQuantity: Quantity, quantityToDelete: Quantity)
  extends Exception(s"Deleting too much quantity [tickPrice:$price][currentQuantity:$currentQuantity][quantityToDelete:$quantityToDelete]")
