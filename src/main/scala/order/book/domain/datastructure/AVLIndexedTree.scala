package order.book.domain.datastructure


sealed trait AVLIndexedTree[+T] {
  val depth: Int
  val level: Int
  val balanceScore: Int
  def insert[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U]
  def delete[U >: T](index: Int): AVLIndexedTree[U]
  def updated[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U]
  def balance: AVLIndexedTree[T]
  def toList: List[T]
}

object AVLIndexedTree {
  def empty[T]: AVLIndexedTree[T] = EmptyTree
}

case object EmptyTree extends AVLIndexedTree[Nothing] {
  val depth: Int = 0
  val level: Int = 0
  val balanceScore: Int = 0

  def insert[T](index: Int, elementToInsert: T): AVLIndexedTree[T] =
    Node(EmptyTree, 0, elementToInsert, 0, EmptyTree)

  def delete[T](index: Int): AVLIndexedTree[T] = EmptyTree

  def updated[T](index: Int, elementToInsert: T): AVLIndexedTree[T] = EmptyTree

  override def toList: List[Nothing] = List.empty

  override def balance: AVLIndexedTree[Nothing] = this
}

case class Node[T](left: AVLIndexedTree[T], leftNumberOfElement: Int,
                   element: T,
                   rightNumberOfElement: Int, right: AVLIndexedTree[T]) extends AVLIndexedTree[T] {


  def isCurrentNode(index: Int): Boolean = leftNumberOfElement == index
  def isInTheLeftSide(index: Int): Boolean = index < leftNumberOfElement
  def isInTheRightSide(index: Int): Boolean = index > leftNumberOfElement

  def computeLeftIndex(index: Int): Int = index
  def computeRightIndex(index: Int): Int = index - leftNumberOfElement - 1

  override def balance: AVLIndexedTree[T] = {

    def leftRotation(tree: AVLIndexedTree[T]): AVLIndexedTree[T] = this match {
      case Node(_, _, _, _, Node(rightLeft, rightLeftNumberOfElement, rightElement, rightRightNumberOfElement, rightRight)) =>
        Node(
          Node(
            left,
            leftNumberOfElement,
            element,
            rightNumberOfElement - rightRightNumberOfElement - 1,
            rightLeft
          ),
          rightLeftNumberOfElement + leftNumberOfElement + 1,
          rightElement,
          rightRightNumberOfElement,
          rightRight
        )
      case _ => this
    }

    def rightLeftRotation(tree: AVLIndexedTree[T]): AVLIndexedTree[T] =  this match {
      case Node(_, _, _, _,
            Node(
              Node(
                rightLeftLeft,
                rightLeftLeftNumberOfElement,
                rightLeftElement,
                rightLeftRightNumberOfElement,
                rightLeftRight
              ),
            rightLeftNumberOfElement,
            rightElement,
            rightRightNumberOfElement,
            rightRight
          )
      ) =>
        Node(
          Node(
            left,
            leftNumberOfElement,
            element,
            rightNumberOfElement - rightRightNumberOfElement - rightLeftRightNumberOfElement - 2,
            rightLeftLeft
          ),
          rightLeftLeftNumberOfElement + leftNumberOfElement + 1,
          rightLeftElement,
          rightRightNumberOfElement + rightLeftRightNumberOfElement + 1,
          Node(
            rightLeftRight,
            rightLeftNumberOfElement - rightLeftLeftNumberOfElement - 1,
            rightElement,
            rightRightNumberOfElement,
            rightRight
          )
        )
      case _ => this
    }

    def rightRotation(tree: AVLIndexedTree[T]): AVLIndexedTree[T] = this match {
      case Node(Node(leftLeft, leftLeftNumberOfElement, leftElement, leftRightNumberOfElement, leftRight), _, _, _, _) =>
        Node(
          leftLeft,
          leftLeftNumberOfElement,
          leftElement,
          leftRightNumberOfElement + rightNumberOfElement + 1,
          Node(
            leftRight,
            leftNumberOfElement - leftLeftNumberOfElement - 1,
            element,
            rightNumberOfElement,
            right
          )
        )
      case _ => this
    }

    def leftRightRotation(tree: AVLIndexedTree[T]): AVLIndexedTree[T] = this match {
      case Node(
            Node(
              leftLeft,
              leftLeftNumberOfElement,
              leftElement,
              leftRightNumberOfElement,
              Node(
                leftRightLeft,
                leftRightLeftNumberOfElement,
                leftRightElement,
                leftRightRightNumberOfElement,
                leftRightRight
              )
            ), _, _, _, _) =>
        Node(
          Node(
            leftLeft,
            leftLeftNumberOfElement,
            leftElement,
            leftRightNumberOfElement - leftRightRightNumberOfElement - 1,
            leftRightLeft
          ),
          leftRightLeftNumberOfElement + leftLeftNumberOfElement + 1,
          leftRightElement,
          leftRightRightNumberOfElement + rightNumberOfElement + 1,
          Node(
            leftRightRight,
            leftNumberOfElement - leftLeftNumberOfElement - leftRightLeftNumberOfElement - 2,
            element,
            rightNumberOfElement,
            right
          )
        )
      case _ => this
    }

    if(balanceScore == -2){
      if(left.balanceScore == -1) rightRotation(this)
      else leftRightRotation(this)
    }else if(balanceScore == 2){
      if(right.balanceScore == 1) leftRotation(this)
      else rightLeftRotation(this)
    }else{
      this
    }
  }

  override def insert[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U] =
    if(isInTheLeftSide(index) || isCurrentNode(index)){
      copy(
        left = left.insert(computeLeftIndex(index), elementToInsert).balance,
        leftNumberOfElement + 1
      ).balance
    }else{
      copy(
        right = right.insert(computeRightIndex(index), elementToInsert).balance,
        rightNumberOfElement = rightNumberOfElement + 1
      ).balance
    }

  def deleteMin: (T, AVLIndexedTree[T]) = this match {
    case Node(EmptyTree, _, _, _, _) => (element, EmptyTree)
    case Node(leftNode @ Node(_, _, _, _, _), _, _, _, _) => leftNode.deleteMin match {
      case (minElement, leftTree) => (minElement, copy(left = leftTree, leftNumberOfElement = leftNumberOfElement - 1))
    }
  }

  override def delete[U >: T](index: Int): AVLIndexedTree[U] = this match {
    case _ if isInTheLeftSide(index) =>
      copy(
        left = left.delete(computeLeftIndex(index)).balance,
        leftNumberOfElement = leftNumberOfElement - 1
      ).balance
    case _ if isInTheRightSide(index) =>
      copy(
        right = right.delete(computeRightIndex(index)).balance,
        rightNumberOfElement = rightNumberOfElement - 1
      ).balance

    // Case one - no children
    case Node(EmptyTree, _, _, _, EmptyTree) => EmptyTree

    // Case two - one child
    case Node(Node(_, _, _, _, _), _, _, _, EmptyTree) => left
    case Node(EmptyTree, _, _, _, Node(_, _,_, _, _)) => right

    // Case tree - two children
    case Node(_, _, _, _, rightNode @ Node(_, _, _, _, _)) =>
      rightNode.deleteMin match {
        case (minElement, rightTree) => Node(left, leftNumberOfElement,  minElement, rightNumberOfElement - 1, rightTree)
      }
  }



  override def updated[U >: T](index: Int, elementToInsert: U): AVLIndexedTree[U] =
    if(isCurrentNode(index)) copy(element = elementToInsert)
    else if (isInTheLeftSide(index)) copy(left = left.updated(computeLeftIndex(index), elementToInsert))
    else copy(right = right.updated(computeRightIndex(index), elementToInsert))


  override def toList: List[T] = left.toList ++ (element +: right.toList)

  lazy val depth: Int = this match {
    case Node(EmptyTree, _, _, _, EmptyTree) => 0
    case _ => 1 + Math.max(left.depth, right.depth)
  }

  lazy val level: Int = depth + 1

  override val balanceScore: Int = right.level - left.level
}



