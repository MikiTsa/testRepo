package com.example.lib

class FitnessCenter(
    val id: Int,
    val name: String,
    val location: String,
    val equipmentList: MutableList<Equipment>
) {
    override fun toString(): String {
        return """
            Fitness Center: $name
            ID: $id
            Location: $location
            Total Equipment: ${equipmentList.size}
        """.trimIndent()
    }
}