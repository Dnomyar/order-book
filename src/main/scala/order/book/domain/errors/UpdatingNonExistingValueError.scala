package order.book.domain.errors

import order.book.domain.{Quantity, TickPrice}

class UpdatingNonExistingValueError(priceLevelIndex: Int, price: TickPrice, quantity: Quantity)
  extends Exception(s"Updating non existing value [priceLevelIndex:$priceLevelIndex][tickPrice:$price][quantity:$quantity]")
