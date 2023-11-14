import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import java.util.Comparator

interface UserTypeKt : Cloneable, Serializable {
    fun typeName(): String
    fun create(): Any
    fun readValue(`in`: InputStreamReader): Any
    fun parseValue(string: String): UserTypeKt?
    fun getTypeComparator(): Comparator<Any>
}

class PointKt(private val x: Int = 0, private val y: Int = 0) : UserTypeKt {
    override fun typeName(): String = "Point"

    override fun create(): Any = PointKt()

    override fun readValue(`in`: InputStreamReader): Any {
        try {
            val reader = BufferedReader(`in`)
            val line = reader.readLine()
            return parseValue(line) ?: PointKt()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return PointKt()
    }

    override fun parseValue(string: String): UserTypeKt? {
        val parts = string.split(",")
        if (parts.size == 2) {
            try {
                val x = parts[0].toInt()
                val y = parts[1].toInt()
                return PointKt(x, y)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun getTypeComparator(): Comparator<Any> = Comparator { o1, o2 ->
        if (o1 is PointKt && o2 is PointKt) {
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
            return PointKt()
        }
    }

    override fun toString(): String = "$x,$y"
}

class FractionKt(private val intPart: Int = 0, private val numerator: Int = 0, private val denominator: Int = 1) : UserTypeKt {
    private val isNegative: Boolean = intPart * numerator * denominator < 0

    init {
        if (denominator == 0) {
            throw ArithmeticException("Denominator cannot be zero")
        }
    }

    override fun typeName(): String = "Fraction"

    override fun create(): Any = FractionKt()

    override fun readValue(`in`: InputStreamReader): Any {
        try {
            val reader = BufferedReader(`in`)
            val line = reader.readLine()
            return parseValue(line) ?: FractionKt()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return FractionKt()
    }

    override fun parseValue(string: String): UserTypeKt? {
        val parts = string.split("/")
        try {
            if (parts.size == 2) {
                val numerator = parts[0].toInt()
                val denominator = parts[1].toInt()
                return FractionKt(numerator, denominator)
            } else if (parts.size == 3) {
                val intPart = parts[0].toInt()
                val numerator = parts[1].toInt()
                val denominator = parts[2].toInt()
                return FractionKt(intPart, numerator, denominator)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getTypeComparator(): Comparator<Any> = Comparator { o1, o2 ->
        if (o1 is FractionKt && o2 is FractionKt) {
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
                return@Comparator f1.intPart.compareTo(f2.intPart)
            } else {
                return@Comparator (f1.numerator * f2.denominator).compareTo(f2.numerator * f1.denominator)
            }
        } else {
            0
        }
    }

    override fun clone(): Any {
        try {
            return super.clone()
        } catch (e: CloneNotSupportedException) {
            return FractionKt()
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
