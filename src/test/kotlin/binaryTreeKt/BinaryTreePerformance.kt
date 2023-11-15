package binaryTreeKt

import kotlin.math.ln
import kotlin.math.pow

class BinaryTreePerformance() : BinaryTree.Abstract() {
    var count: Int = 0
        private set

    override fun balance() {
        count = 0
        val dummy: Node = Node()
        dummy.right = this.root
        treeToVine(dummy)
        vineToTree(dummy, size)
        this.root = dummy.right
        recalculateWeights()
    }

    private fun treeToVine(root: Node) {
        var tail = root
        var rest = tail.right
        while (rest != null) {
            count++
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
            this.count++
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
        count++
        val leftWeight = recalculateWeights(node.left)
        val rightWeight = recalculateWeights(node.right)
        node.weight = leftWeight + rightWeight + 1
        return node.weight
    }
}