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
import com.example.lib.Equipment
import com.example.lib.Brand
import com.example.lib.MuscleGroups
import io.github.serpro69.kfaker.Faker

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val equipmentList = ArrayList<Equipment>()
    private val faker = Faker()

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

                        val brand = Brand(
                            name = brandName,
                            country = faker.address.country(),
                            foundedYear = (1900..2023).random()
                        )

                        val newEquipment = Equipment(
                            name,
                            muscleGroupEnum,
                            weightLimit,
                            price,
                            brand
                        )

                        equipmentList.add(newEquipment)
                        Log.d("MainActivity", "Equipment added: $newEquipment")
                        Toast.makeText(this, "Equipment added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MainActivity", "Intent data was null")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        binding.buttonQR.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            addActivityResultLauncher.launch(intent)
        }

        binding.buttonInfo.setOnClickListener {
            val count = equipmentList.size
            Log.i("MainActivity", "Number of equipment items in list: $count")
        }

        binding.buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.buttonExit.setOnClickListener {
            finish()
        }
    }
}