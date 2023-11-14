package binaryTreeKt

import org.junit.Assert.assertEquals
import org.junit.Test

class BinaryTreeForTest : BinaryTree.Abstract() {

}
class BinaryTreeTest {
    @Test
    fun addTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        assertEquals(true, binaryTree.add(builder!!.parseValue("2")!!))
        assertEquals(true, binaryTree.add(builder.parseValue("3")!!))
        assertEquals(true, binaryTree.add(builder.parseValue("1")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))

        val treeTraverse = buildList {
            binaryTree.forEach(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(1, 2, 3), treeTraverse)
    }

    @Test
    fun deleteTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("2")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("4")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("6")!!)
        binaryTree.add(builder.parseValue("8")!!)

        assertEquals(3, binaryTree.at(2).toString().toInt())
        assertEquals(true,binaryTree.delete(2))
        assertEquals(false, binaryTree.delete(100))
        assertEquals(4, binaryTree.at(2).toString().toInt())
    }

    @Test
    fun atTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("1")!!)
        binaryTree.add(builder.parseValue("2")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("4")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("6")!!)
        binaryTree.add(builder.parseValue("8")!!)

        assertEquals(1, binaryTree.at(0).toString().toInt())
        assertEquals(4, binaryTree.at(3).toString().toInt())
        assertEquals(8, binaryTree.at(6).toString().toInt())
        assertEquals(null, binaryTree.at(2000))
    }

    @Test
    fun balanceTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("1")!!)
        binaryTree.add(builder.parseValue("2")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("4")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("6")!!)
        binaryTree.add(builder.parseValue("8")!!)

        val treeFromRootUnbalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 8),treeFromRootUnbalanced)

        binaryTree.balance()

        val treeFromRootBalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(4, 2, 1, 3, 6, 5, 8),treeFromRootBalanced)
    }

    @Test
    fun equalValuesTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)
        binaryTree.add(builder.parseValue("1")!!)

        assertEquals(1, binaryTree.size)
        assertEquals(1, binaryTree.at(0).toString().toInt())
    }

    @Test
    fun unorderedTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("1")!!)
        binaryTree.add(builder.parseValue("7")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("10")!!)
        binaryTree.add(builder.parseValue("21")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("2")!!)

        val treeFromRootUnbalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(1, 7, 3, 2, 5, 10, 21), treeFromRootUnbalanced)

        binaryTree.balance()

        val treeFromRootBalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(5, 2, 1, 3, 10, 7, 21), treeFromRootBalanced)
    }

    @Test
    fun orderedStraightTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("1")!!)
        binaryTree.add(builder.parseValue("2")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("7")!!)
        binaryTree.add(builder.parseValue("10")!!)
        binaryTree.add(builder.parseValue("21")!!)

        val treeFromRootUnbalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(1, 2, 3, 5, 7, 10, 21), treeFromRootUnbalanced)

        binaryTree.balance()

        val treeFromRootBalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(5, 2, 1, 3, 10, 7, 21), treeFromRootBalanced)
    }

    @Test
    fun orderedReverseTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("21")!!)
        binaryTree.add(builder.parseValue("10")!!)
        binaryTree.add(builder.parseValue("7")!!)
        binaryTree.add(builder.parseValue("5")!!)
        binaryTree.add(builder.parseValue("3")!!)
        binaryTree.add(builder.parseValue("2")!!)
        binaryTree.add(builder.parseValue("1")!!)

        val treeFromRootUnbalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(21, 10, 7, 5, 3, 2, 1), treeFromRootUnbalanced)

        binaryTree.balance()

        val treeFromRootBalanced = buildList {
            binaryTree.forEachFromRoot(object : ElementProcessor<UserType> {
                override fun toDo(v: UserType) {
                    add(v.toString().toInt())
                }
            })
        }

        assertEquals(listOf(5, 2, 1, 3, 10, 7, 21), treeFromRootBalanced)
    }

    @Test
    fun repeatedElementsTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        binaryTree.add(builder!!.parseValue("21")!!)


        binaryTree.add(builder.parseValue("10")!!)
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))

        binaryTree.add(builder.parseValue("7")!!)

        binaryTree.add(builder.parseValue("5")!!)
        assertEquals(false, binaryTree.add(builder.parseValue("5")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))

        binaryTree.add(builder.parseValue("3")!!)


        binaryTree.add(builder.parseValue("2")!!)
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("10")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("5")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))
        assertEquals(false, binaryTree.add(builder.parseValue("2")!!))



        binaryTree.add(builder.parseValue("1")!!)

        assertEquals(binaryTree.size, 7)
    }

    @Test
    fun extremeValuesTest() {
        val binaryTree = BinaryTree.Base()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")
    }
}