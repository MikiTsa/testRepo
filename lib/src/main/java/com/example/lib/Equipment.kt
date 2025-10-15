package com.example.lib

class Equipment(
    val name: String,
    val muscleGroup: MuscleGroups,
    val weightLimit: Int,
    val price: Double,
    val manufacturer: Manufacturer
) : Comparable<Equipment> {

    override fun compareTo(other: Equipment): Int {
        return price.compareTo(other.price)
    }

    override fun toString(): String {
        return "\n(name='$name',\nmuscleGroup='$muscleGroup',\nweightLimit=$weightLimit,\nprice=$price,\nmanufacturer=$manufacturer)\n"
    }
}