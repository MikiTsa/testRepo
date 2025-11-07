package com.example.fitnesscenterapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesscenterapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "FitnessSettings"
        private const val KEY_GYM_NAME = "gym_name"
        private const val KEY_MAX_EQUIPMENT = "max_equipment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        loadSettings()

        binding.btnSave.setOnClickListener {
            saveSettings()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun loadSettings() {
        val gymName = sharedPreferences.getString(KEY_GYM_NAME, "")
        val maxEquipment = sharedPreferences.getInt(KEY_MAX_EQUIPMENT, 100)

        binding.etGymName.setText(gymName)
        binding.etMaxEquipment.setText(maxEquipment.toString())
    }

    private fun saveSettings() {
        val gymName = binding.etGymName.text.toString()
        val maxEquipmentStr = binding.etMaxEquipment.text.toString()

        if (gymName.isEmpty()) {
            Toast.makeText(this, "Please enter gym name", Toast.LENGTH_SHORT).show()
            return
        }

        val maxEquipment = try {
            maxEquipmentStr.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show()
            return
        }

        with(sharedPreferences.edit()) {
            putString(KEY_GYM_NAME, gymName)
            putInt(KEY_MAX_EQUIPMENT, maxEquipment)
            apply()
        }

        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}