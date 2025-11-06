package com.example.fitnesscenterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.fitnesscenterapp.databinding.ActivityMainBinding
import com.example.lib.Brand
import com.example.lib.Equipment
import com.example.lib.MuscleGroups
import io.github.serpro69.kfaker.Faker

/**
 * MainActivity - Main screen of the app
 *
 * CHANGES FOR TASK 4:
 * - Now uses MyApplication for global state (equipmentList moved to MyApplication)
 * - Added Settings button
 * - Uses app.addEquipment() instead of direct list manipulation
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // TASK 4 CHANGE: Access to global application state
    private lateinit var app: MyApplication

    private val faker = Faker()

    /**
     * Activity result launcher for AddActivity and ScannerActivity
     * Receives equipment data and adds it to the global list
     */
    private val addActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val name = data.getStringExtra("name")
                    val weightLimit = data.getIntExtra("weightLimit", -1)
                    val price = data.getDoubleExtra("price", -1.0)
                    val brandName = data.getStringExtra("brandName")
                    val muscleGroupEnum = data.getSerializableExtra("muscleGroup") as MuscleGroups?

                    if (name != null && weightLimit != -1 && price != -1.0 &&
                        brandName != null && muscleGroupEnum != null) {

                        // Generate fake brand data
                        val brand = Brand(
                            name = brandName,
                            country = faker.address.country(),
                            foundedYear = (1900..2023).random()
                        )

                        // Create equipment (UUID is auto-generated in Equipment constructor)
                        val newEquipment = Equipment(
                            name,
                            muscleGroupEnum,
                            weightLimit,
                            price,
                            brand
                        )

                        // TASK 4 CHANGE: Use MyApplication's addEquipment method
                        // This adds to the list AND saves to JSON file automatically
                        app.addEquipment(newEquipment)

                        Log.d("MainActivity", "Equipment added: ${newEquipment.name} with ID: ${newEquipment.id}")
                        Toast.makeText(this, "Equipment added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Intent data was null")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 4 CHANGE: Get reference to MyApplication
        app = application as MyApplication

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply custom font
        applyCustomFont()

        // Show welcome message with equipment count
        // TASK 4 CHANGE: Use app.equipmentList instead of local equipmentList
        val userName = "Mihail"
        val welcomeMessage = getString(R.string.welcome_message, userName, app.equipmentList.size)
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()

        // Setup button click listeners
        setupClickListeners()
    }

    /**
     * Setup all button click listeners
     */
    private fun setupClickListeners() {
        // Add button - opens AddActivity
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        // Info button - logs equipment count
        binding.buttonInfo.setOnClickListener {
            // TASK 4 CHANGE: Use app.equipmentList
            val count = app.equipmentList.size
            Log.i("MainActivity", "Number of equipment items in list: $count")
            Toast.makeText(this, "Equipment count: $count (check Logcat)", Toast.LENGTH_SHORT).show()
        }

        // About button - opens AboutActivity
        binding.buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // QR button - opens ScannerActivity
        binding.buttonQR.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        // TASK 4 NEW: Settings button - opens SettingsActivity
        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Exit button - closes app
        binding.buttonExit.setOnClickListener {
            finish()
        }
    }

    /**
     * Apply custom font to title
     */
    private fun applyCustomFont() {
        try {
            val typeface = ResourcesCompat.getFont(this, R.font.custom_font)
            binding.tvTitle.typeface = typeface
        } catch (e: Exception) {
            Log.e("MainActivity", "Error loading custom font", e)
        }
    }
}