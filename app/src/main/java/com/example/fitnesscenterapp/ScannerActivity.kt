@file:Suppress("DEPRECATION")

package com.example.fitnesscenterapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lib.MuscleGroups
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject

class ScannerActivity : AppCompatActivity() {

    private val addActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, result.data)
                finish()
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
                finish()
            } else {
                handleScannedData(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleScannedData(data: String) {
        try {
            val jsonObject = JSONObject(data)

            if (jsonObject.has("name") && jsonObject.has("muscleGroup") &&
                jsonObject.has("weightLimit") && jsonObject.has("price") &&
                jsonObject.has("brandName")
            ) {
                val name = jsonObject.getString("name")
                val muscleGroupString = jsonObject.getString("muscleGroup")
                val weightLimit = jsonObject.getInt("weightLimit")
                val price = jsonObject.getDouble("price")
                val brandName = jsonObject.getString("brandName")

                val muscleGroup = try {
                    MuscleGroups.valueOf(muscleGroupString)
                } catch (e: IllegalArgumentException) {
                    null
                }

                if (muscleGroup != null) {
                    val intent = Intent(this, AddActivity::class.java).apply {
                        putExtra("name", name)
                        putExtra("muscleGroup", muscleGroup.name)
                        putExtra("weightLimit", weightLimit)
                        putExtra("price", price)
                        putExtra("brandName", brandName)
                        putExtra("fromQR", true)
                    }
                    addActivityResultLauncher.launch(intent)
                } else {
                    Toast.makeText(this, "Invalid muscle group in QR code", Toast.LENGTH_SHORT).show()
                    provideHapticFeedback()
                    finish()
                }
            } else {
                Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show()
                provideHapticFeedback()
                finish()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing QR code: ${e.message}", Toast.LENGTH_SHORT).show()
            provideHapticFeedback()
            finish()
        }
    }

    private fun provideHapticFeedback() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(500)
        }
    }
}