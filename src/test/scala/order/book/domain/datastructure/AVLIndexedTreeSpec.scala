package order.book.domain.datastructure

import org.scalatest.{Matchers, WordSpec}


class AVLIndexedTreeSpec extends WordSpec with Matchers {

  "It" should {
    "be possible to know if a given index is in the left or the right side of the tree" in {
      val node = Node(EmptyTree, 3, '5', 5, EmptyTree)

      node.isInTheLeftSide(2) should be (true)
      node.isInTheLeftSide(5) should be (false)
    }

    "be possible to turn the tree into a List" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .toList should be (
        List('1', '2', '3', '4', '5')
      )
    }
  }

  "An value" should {
    "be addable to an empty tree" in {
      EmptyTree.insert(1, '1').toList should be (List('1'))
    }
    "be added before with the same id" in {
      EmptyTree
        .insert(0, '5')
        .insert(0, '1') should be (
        Node(Node(EmptyTree,0,'1',0,EmptyTree),1,'5',0,EmptyTree)
      )
    }
    "be added after with the same id should " in {
      EmptyTree
        .insert(0, '5')
        .insert(1, '1') should be (
        Node(EmptyTree,0,'5',1,Node(EmptyTree,0,'1',0,EmptyTree))
      )
    }
    "be addable in the middle of the tree" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(4, '4')
        .insert(5, '5')
        .insert(2, '3')
        .toList should be (
        List('1', '2', '3', '4', '5')
      )
    }
  }

  "The depth of the tree" should {
    "be 0" in {
      EmptyTree.depth should be (0)
      Node(EmptyTree, 0, '1', 0, EmptyTree).depth should be (0)
    }
    "be 1" in {
      Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 0, EmptyTree).depth should be (1)
      Node(EmptyTree, 0, '1', 2, Node(EmptyTree, 0, '2', 1, EmptyTree)).depth should be (1)
    }
    "be 2" in {
      Node(Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 0, EmptyTree), 2, '3', 0, EmptyTree).depth should be (2)
      Node(EmptyTree, 0, '1', 2, Node(Node(EmptyTree, 0, '2', 0, EmptyTree), 1, '3', 0, EmptyTree)).depth should be (2)
    }
  }

  "The balance score" should {
    "be 0" in {
      EmptyTree.balanceScore should be (0)
      Node(EmptyTree, 0, '2', 1, EmptyTree).balanceScore should be (0)
    }
    "be 1" in {
      Node(EmptyTree, 0, '1', 2, Node(EmptyTree, 0, '2', 1, EmptyTree)).balanceScore should be (1)
    }
    "be -1" in {
      Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 0, EmptyTree).balanceScore should be (-1)
    }
    "be 2 (1)" in {
      Node(EmptyTree, 0, '1', 2, Node(EmptyTree, 0, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree))).balanceScore should be (2)
    }
    "be 2 (2)" in {
      Node(EmptyTree, 0, '1', 2, Node(Node(EmptyTree, 0, '2', 0, EmptyTree), 1, '3', 0, EmptyTree)).balanceScore should be (2)
    }
    "be -2 (1)" in {
      Node(Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 0, EmptyTree), 2, '3', 0, EmptyTree).balanceScore should be (-2)
    }
    "be -2 (2)" in {
      Node(Node(EmptyTree, 0, '2', 1, Node(EmptyTree, 0, '1', 0, EmptyTree)), 2, '3', 0, EmptyTree).balanceScore should be (-2)
    }
  }

  "A tree" should {
    "be balanced 1" in {
      Node(EmptyTree, 0, '1', 2, Node(EmptyTree, 0, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree)))
        .balance should be (
        Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree))
      )
    }
    "be balanced 2" in {
      Node(EmptyTree, 0, '1', 2, Node(Node(EmptyTree, 0, '2', 0, EmptyTree), 1, '3', 0, EmptyTree))
        .balance should be (
        Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree))
      )
    }
    "be balanced 3" in {
      Node(Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 0, EmptyTree), 2, '3', 0, EmptyTree)
        .balance should be (
        Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree))
      )
    }
    "be balanced 4" in {
      Node(Node(EmptyTree, 0, '1', 1, Node(EmptyTree, 0, '2', 0, EmptyTree)), 2, '3', 0, EmptyTree)
        .balance should be (
        Node(Node(EmptyTree, 0, '1', 0, EmptyTree), 1, '2', 1, Node(EmptyTree, 0, '3', 0, EmptyTree))
      )
    }
  }

  "A leaf of the tree" should {
    "be deletable if this element is a leaf (without children)" in {

      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .delete(1) should be (
        Node(EmptyTree, 0, '1', 0, EmptyTree)
      )


      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .delete(2) should be (
        Node(
          Node(EmptyTree,0,'1',0,EmptyTree),
          1,'2',2,
          Node(EmptyTree,0,'4',1,
            Node(EmptyTree,0,'5',0,EmptyTree)
          )
        )
      )
    }
    "if this element is a leaf (with one child) - 1" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .delete(0)
        .toList should be (
        List('2', '3', '4', '5')
      )
    }

    "if this element is a leaf (with two children)" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .delete(3)
        .toList should be(
        List('1', '2', '3', '5')
      )
    }
  }


  "It" should {
    "be possible to update a simple tree" in {
      EmptyTree
        .insert(0, '1')
        .updated(0, 'a')
        .toList should be (
        List('a')
      )

      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .updated(1, 'a')
        .toList should be (
        List('1', 'a')
      )
      EmptyTree
        .insert(0, '2')
        .insert(0, '1')
        .updated(0, 'a')
        .toList should be (
        List('a', '2')
      )
    }
    "be possible to update a value in the tree - 1" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .updated(0, 'a')
        .toList should be (
        List('a', '2', '3', '4', '5')
      )
    }
    "be possible to update a value in the tree - 2" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .updated(2, 'a')
        .toList should be (
        List('1', '2', 'a', '4', '5')
      )
    }
    "be possible to update a value in the tree - 3" in {
      EmptyTree
        .insert(0, '1')
        .insert(1, '2')
        .insert(3, '3')
        .insert(4, '4')
        .insert(5, '5')
        .updated(4, 'a')
        .toList should be (
        List('1', '2', '3', '4', 'a')
      )
    }
  }


}
