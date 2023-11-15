package binaryTreeKt

import kotlin.math.ln
import kotlin.math.pow

interface BinaryTree {
    fun add(item: UserType): Boolean

    fun delete(index: Int): Boolean

    fun at(index: Int): UserType?

    fun isEmpty(): Boolean

    fun balance()

    fun forEach(processor: ElementProcessor<UserType>)

    fun forEachFromRoot(processor: ElementProcessor<UserType>)

    fun clear()

    abstract class Abstract(protected var root: Node? = null, private var _size: Int = 0, private var comparator: Comparator<Any>? = null) :
        BinaryTree {
        inner class Node(val item: UserType? = null, var left: Node? = null, var right: Node? = null, var weight: Int = 0) {
            init {
                weight = (item?.let { 1 } ?: 0) + (left?.weight ?: 0) + (right?.weight ?: 0)
            }
        }

        var size: Int = _size
            get() = _size
            private set

        override fun add(item: UserType): Boolean {
            if (root == null) {
                comparator = item.getTypeComparator()
                root = Node(item)
                _size++
                return true
            }
            return add(root!!, item)
        }

        private fun add(current: Node, item: UserType): Boolean {
            val comparisonResult = comparator!!.compare(current.item, item)
            if (comparisonResult == 0) {
                restoreWeights(root, item)
                return false
            }
            current.weight += 1
            return if (comparisonResult > 0) {
                if (current.left == null) {
                    current.left = Node(item)
                    _size++
                    true
                } else {
                    add(current.left!!, item)
                }
            } else {
                if (current.right == null) {
                    current.right = Node(item)
                    _size++
                    true
                } else {
                    add(current.right!!, item)
                }
            }
        }

        private fun restoreWeights(current: Node?, item: UserType) {
            if (current == null || comparator!!.compare(current.item, item) == 0) {
                return
            }
            current.weight -= 1
            if (comparator!!.compare(current.item, item) > 0) {
                restoreWeights(current.left, item)
            } else {
                restoreWeights(current.right, item)
            }
        }

        override fun delete(index: Int): Boolean {
            return if (this.root == null || index < 0 || index >= _size) false else delete(root, index, null)
        }

        private fun delete(
            current: Node?,
            index: Int,
            previous: Node?
        ): Boolean {
            val currentIndex = current?.left?.weight ?: 0
            current?.weight = current?.weight?.minus(1) ?: 0
            return if (currentIndex < index) {
                delete(current?.right, index - currentIndex - 1, current)
            } else if (currentIndex > index) {
                delete(current?.left, index, current)
            } else {
                if (current?.left == null || current.right == null) {
                    val newNode: Node? = if (current?.left == null) {
                        current?.right
                    } else {
                        current.left
                    }
                    if (previous != null) {
                        if (previous.left === current) {
                            previous.left = newNode
                        } else {
                            previous.right = newNode
                        }
                    } else {
                        newNode?.weight = root?.weight?.minus(1) ?: 0
                        root = newNode
                    }
                } else {
                    val temp: Node = findMin(current.right!!, current, current)
                    if (current.left !== temp) {
                        temp.left = current.left
                        if (current.right !== temp) temp.right = current.right
                    } else {
                        temp.right = current.right
                        if (current.left !== temp) temp.left = current.left
                    }
                    if (previous != null) {
                        temp.weight = current.weight - 1
                        if (previous.left === current) {
                            previous.left = temp
                        } else {
                            previous.right = temp
                        }
                    } else {
                        temp.weight = root?.weight?.minus(1) ?: 0
                        root = temp
                    }
                }
                _size--
                true
            }
        }

        private fun findMin(
            current: Node,
            previous: Node,
            deletable: Node
        ): Node {
            current.weight -= 1
            if (current.left == null) {
                if (current.right != null && previous !== deletable) previous.left =
                    current.right else if (previous !== deletable) previous.left = null
                return current
            }
            return findMin(current.left!!, current, deletable)
        }

        override fun at(index: Int): UserType? {
            if (index < 0 || index >= _size || root == null) {
                return null
            }

            val currentIndex = if (root!!.left != null) root!!.left!!.weight else 0
            if (currentIndex == index) return root!!.item!!


            return if (currentIndex < index) at(
                root!!.right!!,
                index - currentIndex - 1
            ) else at(root!!.left!!, index)
        }

        private fun at(current: Node, index: Int): UserType? {
            val currentIndex = if (current.left != null) current.left!!.weight else 0
            if (currentIndex == index) return current.item
            return if (currentIndex < index) current.right?.let {
                at(
                    it,
                    index - currentIndex - 1
                )
            } else current.left?.let {
                at(
                    it, index
                )
            }
        }

        override fun isEmpty(): Boolean {
            return _size == 0
        }

        override fun balance() {
            val dummy: Node = Node()
            dummy.right = this.root
            treeToVine(dummy)
            vineToTree(dummy, _size)
            this.root = dummy.right
            recalculateWeights()
        }

        private fun treeToVine(root: Node) {
            var tail = root
            var rest = tail.right
            while (rest != null) {
                if (rest.left == null) {
                    tail = rest
                    rest = rest.right
                } else {
                    val temp = rest.left
                    if (temp != null) {
                        rest.left = temp.right
                    }
                    if (temp != null) {
                        temp.right = rest
                    }
                    rest = temp
                    tail.right = temp
                }
            }
        }

        private fun vineToTree(root: Node, size: Int) {
            var _size = size
            val leaves: Int = ((_size + 1) - 2.toDouble().pow(ln((size + 1).toDouble()) / ln(2.0))).toInt()
            compress(root, leaves)
            _size -= leaves
            while (_size > 1) {
                compress(root, _size / 2)
                _size /= 2
            }
        }

        private fun compress(root: Node?, count: Int) {
            var scanner = root
            for (i in 0 until count) {
                val child = scanner?.right
                if (child != null) {
                    scanner?.right = child.right
                }
                scanner = scanner?.right
                if (child != null) {
                    child.right = scanner?.left
                }
                scanner?.left = child
            }
        }

        private fun recalculateWeights() {
            recalculateWeights(root)
        }

        private fun recalculateWeights(node: Node?): Int {
            if (node == null) {
                return 0
            }
            val leftWeight = recalculateWeights(node.left)
            val rightWeight = recalculateWeights(node.right)
            node.weight = leftWeight + rightWeight + 1
            return node.weight
        }

        override fun forEach(processor: ElementProcessor<UserType>) {
            inOrderTraversal(root, processor)
        }

        private fun inOrderTraversal(node: Node?, processor: ElementProcessor<UserType>) {
            if (node != null) {
                inOrderTraversal(node.left, processor)
                node.item?.let { processor.toDo(it) }
                inOrderTraversal(node.right, processor)
            }
        }

        override fun forEachFromRoot(processor: ElementProcessor<UserType>) {
            fromRootOrder(root, processor)
        }

        private fun fromRootOrder(node: Node?, processor: ElementProcessor<UserType>) {
            if (node != null) {
                node.item?.let { processor.toDo(it) }
                fromRootOrder(node.left, processor)
                fromRootOrder(node.right, processor)
            }
        }

        override fun clear() {
            while (_size != 0) {
                delete(0)
            }
        }

        override fun toString(): String {
            val treeString = StringBuilder()
            buildTreeString(root, 0, treeString)
            return treeString.toString()
        }

        private fun buildTreeString(node: Node?, level: Int, treeString: StringBuilder) {
            if (node != null) {
                buildTreeString(node.right, level + 1, treeString)
                for (i in 0 until level) {
                    treeString.append("         ")
                }
                treeString.append(node.item).append("\n")
                buildTreeString(node.left, level + 1, treeString)
            }
        }
    }


    class Base(root: Node? = null, size: Int = 0, comparator: Comparator<Any>? = null) : Abstract(root, size, comparator)
}