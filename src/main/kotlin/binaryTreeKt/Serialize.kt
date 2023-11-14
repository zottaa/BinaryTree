package binaryTreeKt

import java.io.*

interface Serialize {
    fun serialize(tree: BinaryTree?, filename: String?, type: String?)

    fun deserialize(filename: String?): BinaryTree?

    abstract class Abstract : Serialize {
        override fun serialize(tree: BinaryTree?, filename: String?, type: String?) {
            try {
                PrintWriter(filename).use { writer ->
                    writer.println(type)
                    tree!!.forEachFromRoot(object : ElementProcessor<UserType> {
                        override fun toDo(v: UserType) {
                            writer.print(" ")
                            writer.print(v)
                        }
                    })
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun deserialize(filename: String?): BinaryTree? {
            try {
                BufferedReader(FileReader(filename)).use { bufferedReader ->
                    val type = bufferedReader.readLine() ?: throw IOException("Empty file")
                    val userFactory = UserFactory()
                    require(userFactory.getTypeNameList().contains(type)) { "Wrong type" }
                    var line: String?
                    val tree: BinaryTree = BinaryTree.Base()
                    while (bufferedReader.readLine().also { line = it } != null) {
                        val items = line!!.split(" ")
                        for (item in items) {
                            val builder = userFactory.getBuilderByName(type)
                            val obj = builder!!.parseValue(item)
                            if (obj != null) {
                                tree.add(obj as UserType)
                            }
                        }
                    }
                    return tree
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
    }

    class Base : Abstract() {

    }
}
