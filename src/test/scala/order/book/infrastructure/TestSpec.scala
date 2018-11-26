//package order.book.infrastructure
//
//import org.scalatest.{Matchers, WordSpec}
//
//class TestSpec extends WordSpec with Matchers {
//
//  case class State[S, +A](run: S => (A, S)) {
//    def flatMap[B](f: A => State[S, B]): State[S, B] = State(s => {
//      val (a, s1) = run(s)
//      f(a).run(s1)
//    })
//
//    def map[B](f: A => B): State[S, B] =
//      flatMap(a => unit(f(a)))
//
//
//    def unit[B](b: B): State[S, B] = State(s => (b, s))
//  }
//
//  object State {
//    def get[S]: State[S, S] = State(s => (s, s))
//
//    def set[S](s: S): State[S, Unit] = State(_ => ((), s))
//  }
//
//  type Parser[T] = State[String, T]
//
//
//  def matchChar(p: Char => Boolean): Parser[Char] = {
//
//    State.get[String].flatMap(str => str.headOption match {
//      case None => State.unit()
//      case Some(c) if p(c) => State.set(str.tail)
//      case Some(c) => Left(List(s"Unexpected char $c"))
//    })
//
////    State.get[String].map(_.headOption).map{
////      case None => Left(List("Empty string"))
////      case Some(c) if p(c) => State.set()
////      case Some(c) => Left(List(s"Unexpected char $c"))
////    }
////    for {
////      str <- State.get[String]
////
////      //    str.headOption match {
////      //      case None => Left(List("Empty string"))
////      //      case Some(c) if p(c) => Right(c)
////      //      case Some(c) => Left(List(s"Unexpected char $c"))
////      //    }
////    }
//
//      //???
//  }
//
////  def f = {
////    val whitespace = matchChar(_ == ' ')
////
////    val parseNumber = matchChar(_.isDigit)
////    // N B 1 5 30
////    for{
////      instruction <- matchChar("AUD".contains)
////      _ <- whitespace
////      side <- matchChar("AB".contains)
////      _ <- whitespace
////      _ <- isInstruction
////    } yield a
////  }
//
//
//  "It" should {
//    "match char" in {
//      (for{
//        a <- matchChar(_ == '1')
//        b <- matchChar(_ == '0')
//      } yield (a, b)).run("10")
//    }
//  }
//
//}
