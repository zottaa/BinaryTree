package binaryTreeKt

class UserFactory {
    fun getTypeNameList(): ArrayList<String> {
        return ArrayList(listOf("Point", "Fraction"))
    }

    fun getBuilderByName(name: String?): UserType? {
        return when (name) {
            "Point" -> Point()
            "Fraction" -> Fraction()
            else -> null
        }
    }
}