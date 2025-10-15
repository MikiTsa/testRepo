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
import com.example.lib.Manufacturer
import com.example.lib.MuscleGroups
import io.github.serpro69.kfaker.Faker


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val equipmentList = ArrayList<Equipment>()
    private val faker = Faker()

    companion object {
        const val SCAN_QR_CODE_REQUEST = 1001
    }

    // handle the result from InputActivity
    private val inputActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val name = data.getStringExtra("name")
                    val weightLimit = data.getIntExtra("weightLimit", -1)
                    val price = data.getDoubleExtra("price", -1.0)
                    val manufacturerName = data.getStringExtra("manufacturerName")
                    val muscleGroupEnum = data.getSerializableExtra("muscleGroup") as MuscleGroups?

                    if (name != null && weightLimit != -1 && price != -1.0 && manufacturerName != null && muscleGroupEnum != null) {
                        val manufacturer = Manufacturer(
                            name = manufacturerName,
                            country = faker.address.country(),
                            foundedYear = (1900..2023).random()
                        )

                        val newEquipment = Equipment(
                            name,
                            muscleGroupEnum,
                            weightLimit,
                            price,
                            manufacturer
                        )

                        equipmentList.add(newEquipment)
                        Log.d("MainActivity", "Equipment added: $newEquipment")
                        Toast.makeText(this, "Equipment added!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Failed to add equipment. Missing data!",Toast.LENGTH_SHORT).show()
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

        val userName = "Mihail"
        val welcomeMessage = getString(R.string.welcome_message, userName, equipmentList.size)
        Toast.makeText(this, welcomeMessage, Toast.LENGTH_LONG).show()

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            inputActivityResultLauncher.launch(intent)
        }

        binding.buttonAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.buttonScanQR.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivityForResult(intent, SCAN_QR_CODE_REQUEST)
        }

        binding.buttonExit.setOnClickListener {
            finish()
        }
    }
}
