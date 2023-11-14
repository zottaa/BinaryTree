package binaryTreeKt

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import java.util.Comparator

interface UserType : Cloneable, Serializable {
    fun typeName(): String
    fun create(): Any
    fun readValue(`in`: InputStreamReader): Any
    fun parseValue(string: String): UserType?
    fun getTypeComparator(): Comparator<Any>
}

class Point(private val x: Int = 0, private val y: Int = 0) : UserType {
    override fun typeName(): String = "Point"

    override fun create(): Any = Point()

    override fun readValue(`in`: InputStreamReader): Any {
        try {
            val reader = BufferedReader(`in`)
            val line = reader.readLine()
            return parseValue(line) ?: Point()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Point()
    }

    override fun parseValue(string: String): UserType? {
        val parts = string.split(",")
        if (parts.size == 2) {
            try {
                val x = parts[0].toInt()
                val y = parts[1].toInt()
                return Point(x, y)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun getTypeComparator(): Comparator<Any> = Comparator { o1, o2 ->
        if (o1 is Point && o2 is Point) {
            val p1 = o1
            val p2 = o2
            val distance1 = Math.sqrt(Math.pow(p1.x.toDouble(), 2.0) + Math.pow(p1.y.toDouble(), 2.0))
            val distance2 = Math.sqrt(Math.pow(p2.x.toDouble(), 2.0) + Math.pow(p2.y.toDouble(), 2.0))
            return@Comparator distance1.compareTo(distance2)
        } else {
            0
        }
    }

    override fun clone(): Any {
        try {
            return super.clone()
        } catch (e: CloneNotSupportedException) {
            return Point()
        }
    }

    override fun toString(): String = "$x,$y"
}

class Fraction(
    val numerator: Int = 0,
    val denominator: Int = 1,
    val intPart: Int = 0,
    val isNegative: Boolean = false
) : UserType {
    constructor(intPart: Int, numerator: Int, denominator: Int) : this(
        if (denominator == 0) throw ArithmeticException("Denominator cannot be zero") else numerator,
        denominator,
        intPart,
        intPart * numerator * denominator < 0
    )

    constructor(numerator: Int, denominator: Int) : this(
        if (denominator == 0) throw ArithmeticException("Denominator cannot be zero") else if (numerator > denominator) (numerator - (denominator * (numerator / denominator))) else numerator,
        denominator,
        if (numerator > denominator) (numerator / denominator) else 0,
        numerator * denominator < 0
    )

    override fun typeName(): String = "Fraction"

    override fun create(): Any = Fraction()

    override fun readValue(`in`: InputStreamReader): Any {
        try {
            val reader = BufferedReader(`in`)
            val line = reader.readLine()
            return parseValue(line) ?: Fraction()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Fraction()
    }

    override fun parseValue(string: String): UserType? {
        val parts = string.split("/")
        try {
            if (parts.size == 2) {
                val numerator = parts[0].toInt()
                val denominator = parts[1].toInt()
                return Fraction(numerator, denominator)
            } else if (parts.size == 3) {
                val intPart = parts[0].toInt()
                val numerator = parts[1].toInt()
                val denominator = parts[2].toInt()
                return Fraction(intPart, numerator, denominator)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getTypeComparator(): Comparator<Any> {
        return Comparator { o1, o2 ->
            if (o1 is Fraction && o2 is Fraction) {
                val f1 = o1
                val f2 = o2

                if (f1.isNegative != f2.isNegative) {
                    if (f2.isNegative) {
                        return@Comparator 1
                    } else {
                        return@Comparator -1
                    }
                }
                if (f1.intPart != f2.intPart) {
                    return@Comparator Integer.compare(f1.intPart, f2.intPart)
                } else {
                    return@Comparator Integer.compare(f1.numerator * f2.denominator, f2.numerator * f1.denominator)
                }
            } else {
                return@Comparator 0
            }
        }
    }

    override fun clone(): Any {
        try {
            return super.clone()
        } catch (e: CloneNotSupportedException) {
            return Fraction()
        }
    }

    override fun toString(): String {
        return if (intPart != 0) {
            "${intPart * denominator + numerator}/$denominator"
        } else {
            "$numerator/$denominator"
        }
    }
}

class TestInteger(private val value: Int = 0) : UserType, Cloneable {
    override fun typeName(): String {
        return "Integer"
    }

    override fun create(): Any {
        return TestInteger(0)
    }

    override fun readValue(`in`: InputStreamReader): Any {
        try {
            val reader = BufferedReader(`in`)
            val line = reader.readLine()
            return parseValue(line) ?: TestInteger()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return TestInteger()
    }

    override fun parseValue(string: String): UserType? {
        try {
                return TestInteger(string.toInt())
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getTypeComparator(): Comparator<Any> {
        return Comparator { o1, o2 ->
            if (o1 is TestInteger && o2 is TestInteger) {
                val x1 = o1
                val x2 = o2
                return@Comparator Integer.compare(x1.value, x2.value)
            } else {
                return@Comparator 0
            }
        }
    }

    override fun clone(): Any {
        return super<Cloneable>.clone()
    }

    override fun toString(): String {
        return "$value"
    }


}
