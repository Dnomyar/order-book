package order.book.domain.datastructure


sealed trait BinaryTree[+T] {
  val high: Int
  def insert[U >: T](elementToInsert: U)(implicit ordering: Ordering[U]): BinaryTree[U]
  def delete[U >: T](elementToDelete: U)(implicit ordering: Ordering[U]): BinaryTree[T]
  //def updated[U >: T]()
}

object BinaryTree {


}


case object EmptyTree extends BinaryTree[Nothing] {
  override def delete[U >: Nothing](elementToDelete: U)(implicit ordering: Ordering[U]): BinaryTree[Nothing] = EmptyTree
  val high: Int = 0

  def insert[T](elementToInsert: T)(implicit ordering: Ordering[T]): BinaryTree[T] = Node(elementToInsert)
}

case class Node[T](left: BinaryTree[T], element: T, right: BinaryTree[T]) extends BinaryTree[T] {


  override def insert[U >: T](elementToInsert: U)(implicit ordering: Ordering[U]): BinaryTree[U] =
    if(ordering.lteq(elementToInsert, element)){
      copy(left = left.insert(elementToInsert))
    }else{
      copy(right = right.insert(elementToInsert))
    }

  def deleteMin: (T, BinaryTree[T]) = this match {
    case Node(EmptyTree, _, _) => (element, EmptyTree)
    case Node(leftNode @ Node(_, _, _), _, _) => leftNode.deleteMin match {
      case (minElement, leftTree) => (minElement, copy(left = leftTree))
    }
  }

  override def delete[U >: T](elementToDelete: U)(implicit ordering: Ordering[U]): BinaryTree[T] = this match {
    case _ if ordering.gt(elementToDelete, element) => copy(right = right.delete(elementToDelete))
    case _ if ordering.lt(elementToDelete, element) => copy(left = left.delete(elementToDelete))

    // Case one - no children
    case Node(EmptyTree, _, EmptyTree) if ordering.equiv(elementToDelete, element) => EmptyTree

    // Case two - one child
    case Node(Node(_, _, _), _, EmptyTree) if ordering.equiv(elementToDelete, element) => left
    case Node(EmptyTree, _, Node(_, _, _)) if ordering.equiv(elementToDelete, element) => right

    // Case tree - two children
    case Node(_, _, rightNode @ Node(_, _, _)) if ordering.equiv(elementToDelete, element) =>
      rightNode.deleteMin match {
        case (minElement, rightTree) => Node(left, minElement, rightTree)
      }
  }

  // TODO is O(n) the most efficient ?
  lazy val high: Int = 1 + Math.max(left.high, right.high)

}


object Node {
  def apply[T](element: T): Node[T] = Node(EmptyTree, element, EmptyTree)
}

