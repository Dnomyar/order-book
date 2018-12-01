package order.book.domain.datastructure

import org.scalatest.{Matchers, WordSpec}


class BinaryTreeSpec extends WordSpec with Matchers {

  val tree: BinaryTree[Int] = Node(
    Node(1),
    2,
    Node(Node(3), 4, Node(EmptyTree, 5, Node(6)))
  )

  "A leaf of the tree should be deletable" should {
    "if this element is a leaf (without children)" in {
      val expectedTree: BinaryTree[Int] = Node(
        Node(1),
        2,
        Node(Node(3), 4, Node(5))
      )

      tree.delete(6) should be (expectedTree)
    }
    "if this element is a leaf (with one child) - 1" in {
      val expectedTree: BinaryTree[Int] = Node(
        Node(1),
        2,
        Node(Node(3), 4, Node(6))
      )

      tree.delete(5) should be (expectedTree)
    }
    "if this element is a leaf (with one child) - 2" in {
      val expectedTree: BinaryTree[Int] = Node(
        EmptyTree,
        2,
        Node(Node(3), 4, Node(EmptyTree, 5, Node(6)))
      )

      tree.delete(1) should be (expectedTree)
    }
    "if this element is a leaf (with two children)" in {
      val expectedTree: BinaryTree[Int] = Node(
        Node(1),
        3,
        Node(EmptyTree, 4, Node(EmptyTree, 5, Node(6)))
      )

      tree.delete(2) should be (expectedTree)
    }
  }

  "The minimum element of the tree" should {
    "is the element of the node if no children" in {
      Node(5).deleteMin should be (5, EmptyTree)
    }

    "is the element at the left of the tree" in {
      Node(Node(Node(1), 2, Node(3)), 4, EmptyTree).deleteMin should be (
        1,
        Node(Node(EmptyTree, 2, Node(3)), 4, EmptyTree)
      )
    }
  }


  "The high of the tree" should {
    "be 0" in {
      EmptyTree.high should be (0)
    }
    "be 4" in {
      tree.high should be (4)
    }
  }

  "An value" should {
    "be addable to an empty tree" in {
      EmptyTree.insert(1) should be (Node(1))
    }
    "be addable in the left side of the tree" in {
      tree.insert(-1) should be (Node(
        Node(Node(-1), 1, EmptyTree),
        2,
        Node(Node(3), 4, Node(EmptyTree, 5, Node(6)))
      ))
    }
    "be addable in the right side of the tree" in {
      tree.insert(7) should be (Node(
        Node(1),
        2,
        Node(Node(3), 4, Node(EmptyTree, 5, Node(EmptyTree, 6, Node(7))))
      ))
    }
    "be addable in the middle of the tree" in {
      Node(
        Node(1),
        2,
        Node(Node(4), 5, Node(EmptyTree, 6, Node(7)))
      ).insert(3) should be (Node(
        Node(1),
        2,
        Node(Node(Node(3), 4, EmptyTree), 5, Node(EmptyTree, 6, Node(7)))
      ))
    }
  }

}
