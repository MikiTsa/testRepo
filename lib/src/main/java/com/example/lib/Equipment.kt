package com.example.lib

import java.util.UUID

data class Equipment(
    val name: String,
    val muscleGroup: MuscleGroups,
    val weightLimit: Int,
    val price: Double,
    val brand: Brand,
    val id: String = UUID.randomUUID().toString().replace("-", "")  
) : Comparable<Equipment> {

    override fun compareTo(other: Equipment): Int {
        return price.compareTo(other.price)
    }

    override fun toString(): String {
        return """
            Equipment: $name
              ID: $id
              Muscle Group: $muscleGroup
              Weight Limit: ${weightLimit}kg
              Price: $$price
              Brand: $brand
        """.trimIndent()
    }
}