package com.example.lib

import io.github.serpro69.kfaker.Faker

fun main() {
    val faker = Faker()
    val manufacturers = generateManufacturers(3)
    val equipmentList = generateEquipment(6, manufacturers)
    val fitnessCenter = FitnessCenter(
        id = faker.idNumber.hashCode(),
        name = "CleverFit",
        location = "Maribor",
        equipmentList = equipmentList.toMutableList()
    )

    println("Before Sorting:")
    println(fitnessCenter)

    fitnessCenter.equipmentList.sort()  // Sort by price (Comparable)

    println("\nAfter sorting equipment by price(from lowest to highest):")
    println(fitnessCenter)
}

fun generateManufacturers(n: Int): List<Manufacturer> {
    val faker = Faker()
    val manufacturers = mutableListOf<Manufacturer>()
    for (i in 1..n) {
        manufacturers.add(
            Manufacturer(
                name = faker.company.name(),
                country = faker.address.country(),
                foundedYear = (1900..2023).random()
            )
        )
    }
    return manufacturers
}

fun generateEquipment(n: Int, manufacturers: List<Manufacturer>): List<Equipment> {
    val faker = Faker()
    val equipmentList = mutableListOf<Equipment>()
    for (i in 1..n) {
        equipmentList.add(
            Equipment(
                name = faker.construction.materials(),
                muscleGroup = MuscleGroups.values().random(),
                weightLimit = (8..30).random() * 10,
                price = (100..10000).random() + (0..99).random() / 100.0,
                manufacturer = manufacturers.random()
            )
        )
    }
    return equipmentList
}

