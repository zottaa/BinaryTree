package binaryTreeScala

import java.util.Comparator
import scala.annotation.tailrec

trait BinaryTree {
  def add(item: UserType): Boolean
  def delete(index: Int): Boolean
  def at(index: Int): UserType
  def isEmpty: Boolean
  def balance(): Unit
  def forEach(processor: ElementProcessor[UserType]): Unit
  def forEachFromRoot(processor: ElementProcessor[UserType]): Unit
  def clear(): Unit

  def getSize(): Int

  def addFunctional(item: UserType): BinaryTree
}

object BinaryTree {

  class Node(var left: Node = null, var right: Node = null, var item: UserType = null, var weight: Int = 0) {
    def this(item: UserType) = this(null, null, item, 1)
  }

  abstract class Abstract extends BinaryTree {
    protected var root: Node = _
    var size: Int = _
    var comparator: Comparator[Object] = _

    def this(root: Node) = {
      this()
      this.root = root
      this.size = 1
      this.comparator = root.item.getTypeComparator
    }

    def this(_root: Node, _size: Int, _comparator: Comparator[Object]) = {
      this()
      this.root = _root
      this.size = _size
      this.comparator = _comparator
    }

    override def add(item: UserType): Boolean = {
      if (root == null) {
        this.comparator = item.getTypeComparator
        this.root = new Node(item)
        this.size += 1
        true
      } else {
        add(root, item)
      }
    }

    override def clear(): Unit = {
      while (size != 0) {
        delete(0)
      }
    }

    override def getSize(): Int = size

    override def isEmpty: Boolean = size == 0

    @tailrec
    private def add(current: Node, item: UserType): Boolean = {
      val comparisonResult = comparator.compare(current.item, item)

      if (comparisonResult == 0) {
        restoreWeights(root, item)
        false
      } else {
        current.weight += 1

        if (comparisonResult > 0) {
          if (current.left == null) {
            current.left = new Node(item)
            size += 1
            true
          } else {
            add(current.left, item)
          }
        } else {
          if (current.right == null) {
            current.right = new Node(item)
            size += 1
            true
          } else {
            add(current.right, item)
          }
        }
      }
    }

    @tailrec
    private def restoreWeights(current: Node, item: UserType): Unit = {
      if (current == null || comparator.compare(current.item,item) == 0) {
        return
      }

      current.weight -= 1

      if (comparator.compare(current.item, item) > 0) {
        restoreWeights(current.left, item)
      } else {
        restoreWeights(current.right, item)
      }
    }

    override def addFunctional(item: UserType): BinaryTree = {
      new Abstract(addFunctional(root, item), size + 1, item.getTypeComparator) {}
    }

    private def addFunctional(current: Node, item: UserType): Node = {
      if (current == null) {
        new Node(item)
      } else {
        val comparisonResult = comparator.compare(current.item, item)

        if (comparisonResult == 0) {
          restoreWeights(root, item)
          null
        } else {
          val updatedLeft =
            if (comparisonResult > 0)
              addFunctional(current.left, item)
            else
              current.left

          val updatedRight =
            if (comparisonResult <= 0)
              addFunctional(current.right, item)
            else
              current.right

          new Node(updatedLeft, updatedRight,current.item, current.weight+1)
        }
      }
    }

    override def delete(index: Int): Boolean = {
      if (this.root == null || index < 0 || index >= size)
        false
      else
        delete(this.root, index, null)
    }

    private def findMin(current: Node, previous: Node, deletable: Node): Node = {
      current.weight -= 1
      if (current.left == null) {
        if (current.right != null && previous != deletable)
          previous.left = current.right
        else if (previous != deletable)
          previous.left = null
        current
      } else {
        findMin(current.left, current, deletable)
      }
    }

    private def delete(current: Node, index: Int, previous: Node): Boolean = {
      val currentIndex = if (current.left != null) current.left.weight else 0

      current.weight -= 1
      if (currentIndex < index) {
        delete(current.right, index - currentIndex - 1, current)
      } else if (currentIndex > index) {
        delete(current.left, index, current)
      } else {
        if (current.left == null || current.right == null) {
          val newNode = if (current.left == null) current.right else current.left
          if (previous != null) {
            if (previous.left == current) {
              previous.left = newNode
            } else {
              previous.right = newNode
            }
          } else {
            if (newNode != null)
              newNode.weight = root.weight - 1
            root = newNode
          }
        } else {
          val temp = findMin(current.right, current, current)
          if (current.left != temp) {
            temp.left = current.left
            if (current.right != temp)
              temp.right = current.right
          } else {
            temp.right = current.right
            if (current.left != temp)
              temp.left = current.left
          }
          if (previous != null) {
            temp.weight = current.weight - 1
            if (previous.left == current) {
              previous.left = temp
            } else {
              previous.right = temp
            }
          } else {
            temp.weight = root.weight - 1
            root = temp
          }
        }
        size -= 1
        true
      }
    }

    override def at(index: Int): UserType = {
      if (index < 0 || index >= size || root == null) {
        null
      } else {
        val currentIndex = if (root.left != null) root.left.weight else 0
        if (currentIndex == index)
          return this.root.item

        if (currentIndex < index)
          at(root.right, index - currentIndex - 1)
        else
          at(root.left, index)
      }
    }

    private def at(current: Node, index: Int): UserType = {
      val currentIndex = if (current.left != null) current.left.weight else 0
      if (currentIndex == index)
        return current.item

      if (currentIndex < index)
        at(current.right, index - currentIndex - 1)
      else
        at(current.left, index)
    }

    override def balance(): Unit = {
      val dummy = new Node()
      dummy.right = this.root
      treeToVine(dummy)
      vineToTree(dummy, size)
      root = dummy.right
      recalculateWeights()
    }

    private def treeToVine(root: Node): Unit = {
      var tail = root
      var rest = tail.right
      while (rest != null) {
        if (rest.left == null) {
          tail = rest
          rest = rest.right
        } else {
          val temp = rest.left
          rest.left = temp.right
          temp.right = rest
          rest = temp
          tail.right = temp
        }
      }
    }

    private def vineToTree(root: Node, size: Int): Unit = {
      val leaves = (size + 1 - Math.pow(2, Math.log(size + 1) / Math.log(2))).toInt
      compress(root, leaves)
      var _size = size - leaves
      while (_size > 1) {
        compress(root, _size / 2)
        _size /= 2
      }
    }

    private def compress(root: Node, count: Int): Unit = {
      var scanner = root
      for (_ <- 0 until count) {
        val child = scanner.right
        scanner.right = child.right
        scanner = scanner.right
        child.right = scanner.left
        scanner.left = child
      }
    }

    private def recalculateWeights(): Unit = {
      recalculateWeights(root)
    }

    private def recalculateWeights(node: Node): Int = {
      if (node == null) {
        0
      } else {
        val leftWeight = recalculateWeights(node.left)
        val rightWeight = recalculateWeights(node.right)

        node.weight = leftWeight + rightWeight + 1

        node.weight
      }
    }

    override def forEach(processor: ElementProcessor[UserType]): Unit = {
      inOrderTraversal(root, processor)
    }

    private def inOrderTraversal(node: Node, processor: ElementProcessor[UserType]): Unit = {
      if (node != null) {
        inOrderTraversal(node.left, processor)
        processor.toDo(node.item)
        inOrderTraversal(node.right, processor)
      }
    }

    override def forEachFromRoot(processor: ElementProcessor[UserType]): Unit = {
      fromRootOrder(this.root, processor)
    }

    private def fromRootOrder(node: Node, processor: ElementProcessor[UserType]): Unit = {
      if (node != null) {
        processor.toDo(node.item)
        fromRootOrder(node.left, processor)
        fromRootOrder(node.right, processor)
      }
    }

    override def toString: String = {
      val treeString = new StringBuilder()
      buildTreeString(root, 0, treeString)
      treeString.toString
    }

    private def buildTreeString(node: Node, level: Int, treeString: StringBuilder): Unit = {
      if (node != null) {
        buildTreeString(node.right, level + 1, treeString)
        for (_ <- 0 until level) {
          treeString.append("         ")
        }
        treeString.append(node.item).append("\n")
        buildTreeString(node.left, level + 1, treeString)
      }
    }
  }

  class Base extends Abstract {}
}
