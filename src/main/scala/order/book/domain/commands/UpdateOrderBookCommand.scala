package order.book.domain.commands

import order.book.domain.{Quantity, TickPrice}

case class UpdateOrderBookCommand(instruction: OrderBookInstruction,
                                  side: OrderBookSide,
                                  priceLevelIndex: Int,
                                  price: TickPrice,
                                  quantity: Quantity)
