package com.example.fitnesscenterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesscenterapp.databinding.ActivityAddBinding
import com.example.lib.MuscleGroups

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMuscleGroupSpinner()
        loadDataFromIntent()
        setupSubmitButton()
    }

    private fun loadDataFromIntent() {
        val fromQR = intent.getBooleanExtra("fromQR", false)

        if (fromQR) {
            val name = intent.getStringExtra("name")
            val muscleGroupName = intent.getStringExtra("muscleGroup")
            val weightLimit = intent.getIntExtra("weightLimit", -1)
            val price = intent.getDoubleExtra("price", -1.0)
            val brandName = intent.getStringExtra("brandName")

            name?.let { binding.etEquipmentName.setText(it) }
            if (weightLimit != -1) {
                binding.etWeightLimit.setText(weightLimit.toString())
            }
            if (price != -1.0) {
                binding.etPrice.setText(price.toString())
            }
            brandName?.let { binding.etBrandName.setText(it) }

            muscleGroupName?.let { mgName ->
                val position = MuscleGroups.entries.indexOfFirst { it.name == mgName }
                if (position != -1) {
                    binding.spinnerMuscleGroup.setSelection(position)
                }
            }
        }
    }

    private fun setupMuscleGroupSpinner() {
        val muscleGroupsArray = MuscleGroups.entries.map { it.name }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            muscleGroupsArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMuscleGroup.adapter = adapter
    }

    private fun setupSubmitButton() {
        binding.buttonSubmit.setOnClickListener {
            val name = binding.etEquipmentName.text.toString().trim()
            val weightLimitStr = binding.etWeightLimit.text.toString().trim()
            val priceStr = binding.etPrice.text.toString().trim()
            val brandName = binding.etBrandName.text.toString().trim()

            if (name.isEmpty()) {
                binding.etEquipmentName.error = "Equipment name is required"
                return@setOnClickListener
            }

            val weightLimit = weightLimitStr.toIntOrNull()
            if (weightLimit == null) {
                binding.etWeightLimit.error = "Valid weight limit is required"
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull()
            if (price == null) {
                binding.etPrice.error = "Valid price is required"
                return@setOnClickListener
            }

            if (brandName.isEmpty()) {
                binding.etBrandName.error = "Brand name is required"
                return@setOnClickListener
            }

            val muscleGroup = MuscleGroups.valueOf(
                binding.spinnerMuscleGroup.selectedItem.toString()
            )

            val resultIntent = Intent().apply {
                putExtra("name", name)
                putExtra("muscleGroup", muscleGroup)
                putExtra("weightLimit", weightLimit)
                putExtra("price", price)
                putExtra("brandName", brandName)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Equipment data submitted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}