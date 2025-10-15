package com.example.fitnesscenterapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesscenterapp.databinding.ActivityInputBinding
import com.example.lib.MuscleGroups

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val muscleGroupsArray = MuscleGroups.entries.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, muscleGroupsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMuscleGroup.adapter = adapter

        // Check if data was passed from ScannerActivity and pre-fill the fields
        intent?.let {
            val name = it.getStringExtra("name")
            val muscleGroup = it.getStringExtra("muscleGroup")
            val weightLimit = it.getIntExtra("weightLimit", -1)
            val price = it.getDoubleExtra("price", -1.0)
            val manufacturerName = it.getStringExtra("manufacturerName")

            // pre-fill fields with the data if available
            if (name != null) binding.eqNameText.setText(name)
            if (weightLimit != -1) binding.eqWeightLimTextNum.setText(weightLimit.toString())
            if (price != -1.0) binding.eqPriceTextNumDec.setText(price.toString())
            if (manufacturerName != null) binding.manufNameText.setText(manufacturerName)
            if (muscleGroup != null) {
                val muscleGroupPosition = muscleGroupsArray.indexOf(muscleGroup)
                if (muscleGroupPosition >= 0) {
                    binding.spinnerMuscleGroup.setSelection(muscleGroupPosition)
                }
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val name = binding.eqNameText.text.toString()
            val weightLimit = binding.eqWeightLimTextNum.text.toString().toIntOrNull()
            val price = binding.eqPriceTextNumDec.text.toString().toDoubleOrNull()
            val manufacturerName = binding.manufNameText.text.toString()

            if (name.isNotEmpty() && weightLimit != null && price != null && manufacturerName.isNotEmpty()) {
                val muscleGroup = MuscleGroups.valueOf(binding.spinnerMuscleGroup.selectedItem.toString())

                Log.d("InputActivity", "New Equipment - Name: $name, MuscleGroup: $muscleGroup, WeightLimit: $weightLimit, Price: $price, Manufacturer: $manufacturerName")

                val resultIntent = Intent().apply {
                    putExtra("name", name)
                    putExtra("muscleGroup", muscleGroup)
                    putExtra("weightLimit", weightLimit)
                    putExtra("price", price)
                    putExtra("manufacturerName", manufacturerName)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, "Equipment added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
