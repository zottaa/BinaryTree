package binaryTreeScala

import java.util
import scala.jdk.CollectionConverters._

class UserFactory {
  def getTypeNameList: util.List[String] = List("Point", "Fraction").asJava

  def getBuilderByName(name: String): UserType = name match {
    case "Point"    => new Point
    case "Fraction" => new Fraction
    case _          => null
  }
}