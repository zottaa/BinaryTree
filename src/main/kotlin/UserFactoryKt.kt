class UserFactoryKt {
    fun getTypeNameList(): ArrayList<String> {
        return ArrayList(listOf("Point", "Fraction"))
    }

    fun getBuilderByName(name: String?): UserTypeKt? {
        return when (name) {
            "Point" -> PointKt()
            "Fraction" -> FractionKt()
            else -> null
        }
    }
}