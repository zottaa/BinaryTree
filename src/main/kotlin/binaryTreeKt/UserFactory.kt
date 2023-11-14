package binaryTreeKt

class UserFactory {
    fun getTypeNameList(): ArrayList<String> {
        return ArrayList(listOf("Point", "Fraction", "Integer"))
    }

    fun getBuilderByName(name: String?): UserType? {
        return when (name) {
            "Point" -> Point()
            "Fraction" -> Fraction()
            "Integer" -> TestInteger()
            else -> null
        }
    }
}