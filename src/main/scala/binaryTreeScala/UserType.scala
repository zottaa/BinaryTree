package binaryTreeScala

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.util.Comparator

trait UserType extends Cloneable with Serializable {
  def typeName: String
  def create: Any
  def readValue(in: InputStreamReader): Any
  def parseValue(string: String): UserType
  def getTypeComparator: Comparator[Object]
}

class Point extends UserType {
  private var x: Int = 0
  private var y: Int = 0

  def this(_x: Int, _y: Int) = {
    this()
    x = _x
    y = _y
  }

  override def typeName: String = "Point"
  override def create: Any = new Point
  override def readValue(in: InputStreamReader): Any = {
    try {
      val reader = new BufferedReader(in)
      val line = reader.readLine()
      parseValue(line)
    } catch {
      case e: IOException =>
        e.printStackTrace()
        null
    }
  }

  override def parseValue(string: String): UserType = {
    val parts = string.split(",")
    if (parts.length == 2) {
      try {
        val x = parts(0).toInt
        val y = parts(1).toInt
        new Point(x, y)
      } catch {
        case _: NumberFormatException =>
          null
      }
    } else null
  }

  override def getTypeComparator: Comparator[Object] = (o1: Object, o2: Object) => {
    if (o1.isInstanceOf[Point] && o2.isInstanceOf[Point]) {
      val p1 = o1.asInstanceOf[Point]
      val p2 = o2.asInstanceOf[Point]
      val distance1 = Math.sqrt(Math.pow(p1.x, 2) + Math.pow(p1.y, 2))
      val distance2 = Math.sqrt(Math.pow(p2.x, 2) + Math.pow(p2.y, 2))
      java.lang.Double.compare(distance1, distance2)
    } else 0
  }

  override def clone(): AnyRef = {
    try {
      super.clone()
    } catch {
      case _: CloneNotSupportedException =>
        null
    }
  }

  override def toString: String = s"$x,$y"
}

class Fraction(
                val numerator: Int = 0,
                val denominator: Int = 1,
                val intPart: Int = 0,
                val isNegative: Boolean = false
              ) extends UserType {

  def this(intPart: Int, numerator: Int, denominator: Int) = this(
    if (denominator == 0) throw new ArithmeticException("Denominator cannot be zero") else numerator,
    denominator,
    intPart,
    intPart * numerator * denominator < 0
  )

  def this(numerator: Int, denominator: Int) = this(
    if (denominator == 0) throw new ArithmeticException("Denominator cannot be zero") else if (numerator > denominator) numerator - (denominator * (numerator / denominator)) else numerator,
    denominator,
    if (numerator > denominator) numerator / denominator else 0,
    numerator * denominator < 0
  )

  override def typeName(): String = "Fraction"

  override def create(): Any = new Fraction()

  override def readValue(`in`: InputStreamReader): Any = {
    try {
      val reader = new BufferedReader(`in`)
      val line = reader.readLine()
      Option(parseValue(line)).getOrElse(new Fraction())
    } catch {
      case e: IOException =>
        e.printStackTrace()
        new Fraction()
    }
  }

  override def parseValue(string: String): UserType = {
    val parts = string.split("/")
    try {
      if (parts.length == 2) {
        val numerator = parts(0).toInt
        val denominator = parts(1).toInt
        new Fraction(numerator, denominator)
      } else if (parts.length == 3) {
        val intPart = parts(0).toInt
        val numerator = parts(1).toInt
        val denominator = parts(2).toInt
        new Fraction(intPart, numerator, denominator)
      } else {
        null
      }
    } catch {
      case e: NumberFormatException =>
        e.printStackTrace()
        null
    }
  }

  override def getTypeComparator(): Comparator[Object] = (o1: Any, o2: Any) => {
    if (o1.isInstanceOf[Fraction] && o2.isInstanceOf[Fraction]) {
      val f1 = o1.asInstanceOf[Fraction]
      val f2 = o2.asInstanceOf[Fraction]

      if (f1.isNegative != f2.isNegative) {
        if (f2.isNegative) 1
        else -1
      } else if (f1.intPart != f2.intPart) {
        Integer.compare(f1.intPart, f2.intPart)
      } else {
        Integer.compare(f1.numerator * f2.denominator, f2.numerator * f1.denominator)
      }
    } else {
      0
    }
  }

  override def clone(): Any = {
    try {
      super.clone()
    } catch {
      case e: CloneNotSupportedException =>
        new Fraction()
    }
  }

  override def toString: String = {
    if (intPart != 0) {
      s"${intPart * denominator + numerator}/$denominator"
    } else {
      s"$numerator/$denominator"
    }
  }
}




