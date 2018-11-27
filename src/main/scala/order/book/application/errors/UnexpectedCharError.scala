package order.book.application.errors

class UnexpectedCharError(expectedChars: List[Char])
  extends Exception(s"Expected one of $expectedChars")