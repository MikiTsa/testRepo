package com.example.fitnesscenterapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.lib.Equipment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.UUID

// Constants for file names
const val MY_SP_FILE_NAME = "myshared.data"
const val MY_FILE_NAME = "equipment_data.json"

/**
 * MyApplication - Global Application State (Task 4 Requirement 1)
 *
 * This class manages:
 * 1. Global equipmentList (shared across all activities)
 * 2. JSON file persistence (save/load/delete/update)
 * 3. SharedPreferences for app UUID
 * 4. Application-wide initialization
 */
class MyApplication : Application() {

    // Global equipment list (Requirement 1: Global state)
    val equipmentList = ArrayList<Equipment>()

    // SharedPreferences for app UUID (Requirement 5)
    private lateinit var sharedPref: SharedPreferences

    // Gson for JSON serialization (Requirement 3)
    private lateinit var gson: Gson

    // File for JSON storage (Requirement 3)
    private lateinit var file: File

    /**
     * Called when application is created - before any activity
     * This is where we initialize everything
     */
    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "MyApplication onCreate() - Initializing app")

        // Initialize Gson for JSON serialization
        gson = Gson()

        // Initialize file path for JSON storage
        // This file will be in internal storage: /data/data/com.example.fitnesscenterapp/files/
        file = File(filesDir, MY_FILE_NAME)
        Log.d(TAG, "JSON file path: ${file.absolutePath}")

        // Initialize SharedPreferences
        initSharedPreferences()

        // Generate and save app UUID if this is first launch (Requirement 5)
        if (!containsID()) {
            val newUUID = UUID.randomUUID().toString().replace("-", "")
            saveID(newUUID)
            Log.d(TAG, "First launch - Generated new UUID: $newUUID")
        } else {
            Log.d(TAG, "App UUID already exists: ${getID()}")
        }

        // Load equipment data from JSON file (Requirement 3)
        loadFromFile()
    }

    // ============================================================================
    // REQUIREMENT 3: JSON Persistence Functions (Save/Load/Delete/Update)
    // ============================================================================

    /**
     * Save all equipment to JSON file
     * File location: /data/data/com.example.fitnesscenterapp/files/equipment_data.json
     */
    fun saveToFile() {
        try {
            val jsonString = gson.toJson(equipmentList)
            FileUtils.writeStringToFile(file, jsonString, "UTF-8")
            Log.d(TAG, "Successfully saved ${equipmentList.size} items to file")
            Log.d(TAG, "File content: $jsonString")
        } catch (e: IOException) {
            Log.e(TAG, "Error saving to file: ${file.absolutePath}", e)
        }
    }

    /**
     * Load equipment from JSON file
     * Called automatically in onCreate()
     */
    fun loadFromFile() {
        try {
            if (file.exists()) {
                val jsonString = FileUtils.readFileToString(file, "UTF-8")
                Log.d(TAG, "Reading from file: $jsonString")

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

    /**
     * Add equipment to list and save to file
     */
    fun addEquipment(equipment: Equipment) {
        equipmentList.add(equipment)
        saveToFile()
        Log.d(TAG, "Added equipment: ${equipment.name} with ID: ${equipment.id}")
    }

    /**
     * Delete equipment by UUID and save to file
     * @param id The UUID of the equipment to delete
     */
    fun deleteEquipment(id: String) {
        val removed = equipmentList.removeIf { it.id == id }
        if (removed) {
            saveToFile()
            Log.d(TAG, "Deleted equipment with ID: $id")
        } else {
            Log.w(TAG, "Equipment with ID $id not found")
        }
    }

    /**
     * Update existing equipment and save to file
     * @param updatedEquipment The equipment with updated values (same ID)
     */
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

    /**
     * Find equipment by UUID
     * @param id The UUID to search for
     * @return Equipment if found, null otherwise
     */
    fun findEquipmentById(id: String): Equipment? {
        return equipmentList.find { it.id == id }
    }

    // ============================================================================
    // REQUIREMENT 5: SharedPreferences for App UUID
    // ============================================================================

    /**
     * Initialize SharedPreferences
     * File location: /data/data/com.example.fitnesscenterapp/shared_prefs/myshared.data.xml
     */
    private fun initSharedPreferences() {
        sharedPref = getSharedPreferences(MY_SP_FILE_NAME, Context.MODE_PRIVATE)
        Log.d(TAG, "SharedPreferences initialized")
    }

    /**
     * Save app UUID to SharedPreferences
     * This happens only once on first app launch
     */
    private fun saveID(id: String) {
        with(sharedPref.edit()) {
            putString("ID", id)
            apply()
        }
        Log.d(TAG, "Saved UUID to SharedPreferences: $id")
    }

    /**
     * Check if app UUID exists in SharedPreferences
     * @return true if UUID exists, false if first launch
     */
    private fun containsID(): Boolean {
        return sharedPref.contains("ID")
    }

    /**
     * Get app UUID from SharedPreferences
     * @return UUID string or "No ID" if not found
     */
    fun getID(): String {
        return sharedPref.getString("ID", "No ID") ?: "No ID"
    }

    companion object {
        private const val TAG = "MyApplication"
    }
}