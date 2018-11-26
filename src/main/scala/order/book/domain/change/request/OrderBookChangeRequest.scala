package order.book.domain.change.request

import order.book.domain.{Quantity, TickPrice}

case class OrderBookChangeRequest(instruction: OrderBookInstruction,
                                  side: OrderBookSide,
                                  priceLevelIndex: Int,
                                  price: TickPrice,
                                  quantity: Quantity)
