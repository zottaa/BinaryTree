package binaryTreeScala

import java.io.{BufferedReader, FileNotFoundException, FileReader, IOException, PrintWriter}

trait Serialize {
  def serialize(tree: BinaryTree, filename: String, `type`: String): Unit

  def deserialize(filename: String): BinaryTree
}

object Serialize {
  abstract class Abstract extends Serialize {
    override def serialize(tree: BinaryTree, filename: String, `type`: String): Unit = {
      try {
        val writer = new PrintWriter(filename)
        writer.println(`type`)
        tree.forEachFromRoot(new ElementProcessor[UserType] {
          override def toDo(v: UserType): Unit = {
            writer.print(" ")
            writer.print(v)
          }
        })
        writer.close()
      } catch {
        case e: FileNotFoundException =>
          e.printStackTrace()
      }
    }

    override def deserialize(filename: String): BinaryTree = {
      try {
        val bufferedReader = new BufferedReader(new FileReader(filename))
        val `type` = bufferedReader.readLine()
        val userFactory = new UserFactory
        if (!userFactory.getTypeNameList.contains(`type`)) {
          throw new IllegalArgumentException("Wrong type")
        }

        var line: String = null
        val tree = new BinaryTree.Base
        while ( {
          line = bufferedReader.readLine();
          line
        } != null) {
          val items = line.split(" ")

          for (item <- items) {
            val builder = userFactory.getBuilderByName(`type`)
            val `object` = builder.parseValue(item)
            if (`object` != null) {
              tree.add(`object`.asInstanceOf[UserType])
            }
          }
        }
        tree
      } catch {
        case e: IOException =>
          e.printStackTrace()
          null
      }
    }
  }
  class Base extends Abstract {}
}



