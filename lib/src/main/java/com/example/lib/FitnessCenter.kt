package com.example.lib

class FitnessCenter(
    val id: Int,
    val name: String,
    val location: String,
    val equipmentList: MutableList<Equipment>
) {
    fun addEquipment(equipment: Equipment) {
        equipmentList.add(equipment)
    }

    override fun toString(): String {
        return "FitnessCenter (id = $id, \nname = '$name',\nlocation = '$location',\nequipmentList = $equipmentList)\n"
    }
}