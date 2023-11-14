import java.io.*

interface SerializeKt {
    fun serialize(tree: BinaryTreeKt?, filename: String?, type: String?)

    fun deserialize(filename: String?): BinaryTreeKt?

    abstract class Abstract : SerializeKt {
        override fun serialize(tree: BinaryTreeKt?, filename: String?, type: String?) {
            try {
                PrintWriter(filename).use { writer ->
                    writer.println(type)
                    tree!!.forEachFromRoot(object : ElementProcessorKt<UserTypeKt> {
                        override fun toDo(v: UserTypeKt) {
                            writer.print(" ")
                            writer.print(v)
                        }
                    })
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun deserialize(filename: String?): BinaryTreeKt? {
            try {
                BufferedReader(FileReader(filename)).use { bufferedReader ->
                    val type = bufferedReader.readLine() ?: throw IOException("Empty file")
                    val userFactoryKt = UserFactoryKt()
                    require(userFactoryKt.getTypeNameList().contains(type)) { "Wrong type" }
                    var line: String?
                    val tree: BinaryTreeKt = BinaryTreeKt.Base()
                    while (bufferedReader.readLine().also { line = it } != null) {
                        val items = line!!.split(" ")
                        for (item in items) {
                            val builder = userFactoryKt.getBuilderByName(type)
                            val obj = builder!!.parseValue(item)
                            if (obj != null) {
                                tree.add(obj as UserTypeKt)
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
