package com.example.lib

import io.github.serpro69.kfaker.Faker

fun main() {
    val faker = Faker()
    val brands = generateBrands(5)
    val equipmentList = generateEquipment(6, brands)
    val fitnessCenter = FitnessCenter(
        id = faker.idNumber.hashCode(),
        name = "CleverFit",
        location = "Maribor",
        equipmentList = equipmentList.toMutableList()
    )

    println(fitnessCenter)
    println("\n" + "=".repeat(50))
    println("EQUIPMENT INVENTORY (Unsorted)")
    println("=".repeat(50) + "\n")

    fitnessCenter.equipmentList.forEach {
        println(it)
        println("-".repeat(40))
    }

    fitnessCenter.equipmentList.sort()  // comparable po ceno

    println("\n" + "=".repeat(50))
    println("EQUIPMENT INVENTORY (Sorted by Price)")
    println("=".repeat(50) + "\n")

    fitnessCenter.equipmentList.forEach {
        println(it)
        println("-".repeat(40))
    }
}

fun generateBrands(n: Int): List<Brand> {
    val faker = Faker()
    val brands = mutableListOf<Brand>()
    for (i in 1..n) {
        brands.add(
            Brand(
                name = faker.company.name(),
                country = faker.address.country(),
                foundedYear = (1900..2023).random()
            )
        )
    }
    return brands
}

fun generateEquipmentNames(n: Int): List<String> {
    val equipmentTypes = listOf(
        "Leg Press", "Chest Press", "Lat Pulldown", "Cable Machine",
        "Smith Machine", "Squat Rack", "Bench Press",
        "Barbell Set", "Leg Extension", "Leg Curl", "Shoulder Press",
        "Hack Squat", "Calf Raise Machine", "Ab Crunch Machine"
    )

    return (1..n).map {
        equipmentTypes.random()
    }
}

fun generateEquipment(n: Int, brands: List<Brand>): List<Equipment> {
    val equipmentNames = generateEquipmentNames(n)
    val equipmentList = mutableListOf<Equipment>()

    for (name in equipmentNames) {
        equipmentList.add(
            Equipment(
                name = name,
                muscleGroup = MuscleGroups.values().random(),
                weightLimit = (8..30).random() * 10,
                price = (100..10000).random() + (0..99).random() / 100.0,
                brand = brands.random()
            )
        )
    }
    return equipmentList
}