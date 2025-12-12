@file:Suppress("DEPRECATION")

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
import com.example.fitnesscenterapp.databinding.ActivityMainBinding
import com.example.lib.Brand
import com.example.lib.Equipment
import com.example.lib.MuscleGroups
import io.github.serpro69.kfaker.Faker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var app: MyApplication
    private val faker = Faker()
    private lateinit var pagerAdapter: MainPagerAdapter

    private val addActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleAddActivityResult(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MyApplication

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()

        val userName = "Mihail"
        val welcomeMessage = getString(R.string.welcome_message, userName, app.equipmentList.size)
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()
    }

    private fun setupViewPager() {
        pagerAdapter = MainPagerAdapter(
            mainActivity = this,
            app = app,
            onMenuButtonClick = { action -> handleMenuAction(action) },
            onEquipmentClick = { equipment -> handleEquipmentClick(equipment) }
        )

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(0, false)
    }

    private fun handleMenuAction(action: String) {
        when (action) {
            "ADD" -> {
                val intent = Intent(this, AddActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }
            "QR" -> {
                val intent = Intent(this, ScannerActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }
            "INFO" -> {
                val count = app.equipmentList.size
                Log.i("MainActivity", "Number of equipment items in list: $count")
                Toast.makeText(this, "Equipment count: $count (check Logcat)", Toast.LENGTH_SHORT).show()
            }
            "ABOUT" -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            "EXIT" -> {
                finish()
            }
        }
    }

    private fun handleEquipmentClick(equipment: Equipment) {
        val intent = Intent(this, AddActivity::class.java).apply {
            putExtra("equipmentId", equipment.id)
            putExtra("name", equipment.name)
            putExtra("muscleGroup", equipment.muscleGroup.name)
            putExtra("weightLimit", equipment.weightLimit)
            putExtra("price", equipment.price)
            putExtra("brandName", equipment.brand.name)
            putExtra("isEditing", true)
        }
        addActivityResultLauncher.launch(intent)
    }

    private fun handleAddActivityResult(data: Intent?) {
        if (data != null) {
            val isEditing = data.getBooleanExtra("isEditing", false)
            val equipmentId = data.getStringExtra("equipmentId")
            val name = data.getStringExtra("name")
            val weightLimit = data.getIntExtra("weightLimit", -1)
            val price = data.getDoubleExtra("price", -1.0)
            val brandName = data.getStringExtra("brandName")
            val muscleGroupEnum = data.getSerializableExtra("muscleGroup") as MuscleGroups?

            if (name != null && weightLimit != -1 && price != -1.0 &&
                brandName != null && muscleGroupEnum != null) {

                val brand = Brand(
                    name = brandName,
                    country = faker.address.country(),
                    foundedYear = (1900..2023).random()
                )

                if (isEditing && equipmentId != null) {
                    // Update existing equipment
                    val updatedEquipment = Equipment(
                        name = name,
                        muscleGroup = muscleGroupEnum,
                        weightLimit = weightLimit,
                        price = price,
                        brand = brand,
                        id = equipmentId
                    )
                    app.updateEquipment(updatedEquipment)
                    Toast.makeText(this, "Equipment updated successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    // Add new equipment
                    val newEquipment = Equipment(
                        name = name,
                        muscleGroup = muscleGroupEnum,
                        weightLimit = weightLimit,
                        price = price,
                        brand = brand
                    )
                    app.addEquipment(newEquipment)
                    Toast.makeText(this, "Equipment added successfully!", Toast.LENGTH_SHORT).show()
                }

                pagerAdapter.refreshList(1)
                
                Log.d("MainActivity", "Equipment processed: $name")
            }
        } else {
            Log.e("MainActivity", "Intent data was null")
        }
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter.refreshList(1)
    }
}
