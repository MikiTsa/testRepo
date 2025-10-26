package com.example.lib

class Equipment(
    val name: String,
    val muscleGroup: MuscleGroups,
    val weightLimit: Int,
    val price: Double,
    val brand: Brand
) : Comparable<Equipment> {

    override fun compareTo(other: Equipment): Int {
        return price.compareTo(other.price)
    }

    override fun toString(): String {
        return """
            Equipment: $name
              Muscle Group: $muscleGroup
              Weight Limit: ${weightLimit}kg
              Price: $$price
              Brand: $brand
        """.trimIndent()
    }
}