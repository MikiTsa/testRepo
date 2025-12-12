package com.example.fitnesscenterapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.lib.Brand
import com.example.lib.Equipment
import com.example.lib.MuscleGroups
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.serpro69.kfaker.Faker
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.UUID

const val MY_SP_FILE_NAME = "myshared.data"
const val MY_FILE_NAME = "equipment_data.json"

class MyApplication : Application() {

    val equipmentList = ArrayList<Equipment>()

    private lateinit var sharedPref: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var file: File
    private val faker = Faker()

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "MyApplication onCreate() - Initializing app")

        gson = Gson()
        file = File(filesDir, MY_FILE_NAME)
        Log.d(TAG, "JSON file path: ${file.absolutePath}")

        initSharedPreferences()

        // Generate and save app UUID if this is first launch
        if (!containsID()) {
            val newUUID = UUID.randomUUID().toString().replace("-", "")
            saveID(newUUID)
            Log.d(TAG, "First launch - Generated new UUID: $newUUID")
        } else {
            Log.d(TAG, "App UUID already exists: ${getID()}")
        }

        loadFromFile()

        // Generate 100 items
        if (equipmentList.isEmpty()) {
            Log.d(TAG, "Generating 100 equipment items...")
            generate100Equipment()
            saveToFile()
            Log.d(TAG, "Generated and saved 100 equipment items")
        }
    }

    fun saveToFile() {
        try {
            val jsonString = gson.toJson(equipmentList)
            FileUtils.writeStringToFile(file, jsonString, "UTF-8")
            Log.d(TAG, "Successfully saved ${equipmentList.size} items to file")
        } catch (e: IOException) {
            Log.e(TAG, "Error saving to file: ${file.absolutePath}", e)
        }
    }

    fun loadFromFile() {
        try {
            if (file.exists()) {
                val jsonString = FileUtils.readFileToString(file, "UTF-8")
                Log.d(TAG, "Reading from file...")

                val type = object : TypeToken<ArrayList<Equipment>>() {}.type
                val loadedList: ArrayList<Equipment> = gson.fromJson(jsonString, type)

                equipmentList.clear()
                equipmentList.addAll(loadedList)

                Log.d(TAG, "Successfully loaded ${equipmentList.size} items from file")
            } else {
                Log.d(TAG, "No existing data file - starting with empty list")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error loading from file", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON", e)
        }
    }

    fun addEquipment(equipment: Equipment) {
        equipmentList.add(equipment)
        saveToFile()
        Log.d(TAG, "Added equipment: ${equipment.name} with ID: ${equipment.id}")
    }

    fun deleteEquipment(id: String) {
        val removed = equipmentList.removeIf { it.id == id }
        if (removed) {
            saveToFile()
            Log.d(TAG, "Deleted equipment with ID: $id")
        } else {
            Log.w(TAG, "Equipment with ID $id not found")
        }
    }

    fun updateEquipment(updatedEquipment: Equipment) {
        val index = equipmentList.indexOfFirst { it.id == updatedEquipment.id }
        if (index != -1) {
            equipmentList[index] = updatedEquipment
            saveToFile()
            Log.d(TAG, "Updated equipment with ID: ${updatedEquipment.id}")
        } else {
            Log.w(TAG, "Equipment with ID ${updatedEquipment.id} not found for update")
        }
    }

    fun findEquipmentById(id: String): Equipment? {
        return equipmentList.find { it.id == id }
    }

    private fun generate100Equipment() {
        val brands = generateBrands(10)
        val equipmentNames = generateEquipmentNames(100)

        for (name in equipmentNames) {
            val equipment = Equipment(
                name = name,
                muscleGroup = MuscleGroups.entries.random(),
                weightLimit = (8..30).random() * 10,
                price = (100..10000).random() + (0..99).random() / 100.0,
                brand = brands.random()
            )
            equipmentList.add(equipment)
        }
    }

    private fun generateBrands(n: Int): List<Brand> {
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

    private fun generateEquipmentNames(n: Int): List<String> {
        val equipmentTypes = listOf(
            "Leg Press", "Chest Press", "Lat Pulldown", "Cable Machine",
            "Smith Machine", "Squat Rack", "Bench Press",
            "Barbell Set", "Leg Extension", "Leg Curl", "Shoulder Press",
            "Hack Squat", "Calf Raise Machine", "Ab Crunch Machine",
            "Rowing Machine", "Treadmill", "Elliptical", "Stationary Bike"
        )

        return (1..n).map {
            equipmentTypes.random() + " #$it"
        }
    }

    private fun initSharedPreferences() {
        sharedPref = getSharedPreferences(MY_SP_FILE_NAME, Context.MODE_PRIVATE)
        Log.d(TAG, "SharedPreferences initialized")
    }

    private fun saveID(id: String) {
        with(sharedPref.edit()) {
            putString("ID", id)
            apply()
        }
        Log.d(TAG, "Saved UUID to SharedPreferences: $id")
    }

    private fun containsID(): Boolean {
        return sharedPref.contains("ID")
    }

    fun getID(): String {
        return sharedPref.getString("ID", "No ID") ?: "No ID"
    }

    companion object {
        private const val TAG = "MyApplication"
    }
}
