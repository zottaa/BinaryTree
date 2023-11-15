package binaryTreeKt

import java.time.Duration
import java.time.Instant

class BinaryTreePerformanceAndMemoryTest {

    private fun generateIntSet(size: Int): List<Int> {
        val intSet = mutableSetOf<Int>()

        for (i in 0..<size) {
            intSet.add(i)
        }
        return intSet.shuffled()
    }


    fun testPerformance(valueList: List<Int>) {
        val binaryTree = BinaryTreePerformance()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        valueList.forEach {
            binaryTree.add(builder!!.parseValue(it.toString())!!)
        }

        val start = Instant.now()
            binaryTree.balance()
        val end = Instant.now()

        println("Performance test")
        println("Size: ${binaryTree.size}")
        println("Time: ${(Duration.between(start, end).toMillis()).toDouble()/1000} sec")
        println("Operations count: ${binaryTree.count}")
    }

    fun testMemory(valueList: List<Int>) {
        val binaryTree = BinaryTreePerformance()
        val userFactory = UserFactory()
        val builder = userFactory.getBuilderByName("Integer")

        valueList.forEach {
            binaryTree.add(builder!!.parseValue(it.toString())!!)
        }

        println("Memory test")
        println("Size: ${binaryTree.size}")
        println("Heap size: ${Runtime.getRuntime().totalMemory()} bytes")

    }

    fun test() {
        var i = 1000
        while (i < 100000000) {
            val list = generateIntSet(i)
            testPerformance(list)
            println()
            testMemory(list)
            repeat(30, action = {
                print("-")
            })
            println()
            i *= 10
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val test = BinaryTreePerformanceAndMemoryTest()
            test.test()
        }
    }
}
