package com.example.fitnesscenterapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lib.MuscleGroups
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject


@Suppress("DEPRECATION")
class ScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize scanner
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)  // use back camera
        integrator.setBeepEnabled(true)  // beep when QR code is scanned
        integrator.setBarcodeImageEnabled(true)  // display scanned barcode
        integrator.initiateScan()  // start the QR code scanning
    }

    // handle the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) { // unsuccessful
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
            } else {
                handleScannedData(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleScannedData(data: String) {
        try {
            val jsonObject = JSONObject(data) // convert the scanned string into a JSON object

            if (jsonObject.has("name") && jsonObject.has("muscleGroup") &&
                jsonObject.has("weightLimit") && jsonObject.has("price") &&
                jsonObject.has("manufacturerName")
            ) {
                val name = jsonObject.getString("name")
                val muscleGroupString = jsonObject.getString("muscleGroup")
                val weightLimit = jsonObject.getInt("weightLimit")
                val price = jsonObject.getDouble("price")
                val manufacturerName = jsonObject.getString("manufacturerName")

                Log.d("ScannerActivity", "Parsed Data - Name: $name, MuscleGroup: $muscleGroupString, WeightLimit: $weightLimit, Price: $price, Manufacturer: $manufacturerName")

                // convert muscleGroup string to the corresponding enum
                val muscleGroup = try {
                    MuscleGroups.valueOf(muscleGroupString)
                } catch (e: IllegalArgumentException) {
                    null
                }

                // validate muscle group conversion
                if (muscleGroup != null) {
                    val intent = Intent(this, InputActivity::class.java).apply {
                        putExtra("name", name)
                        putExtra("muscleGroup", muscleGroup.name) // pass enum as String
                        putExtra("weightLimit", weightLimit)
                        putExtra("price", price)
                        putExtra("manufacturerName", manufacturerName)
                    }
                    Toast.makeText(this, "Data from QR code read succesfully!", Toast.LENGTH_SHORT).show()
                    startActivity(intent) // sttart InputActivity with pre-filled data
                    finish()
                } else {
                    Toast.makeText(this, "Invalid muscle group in QR code", Toast.LENGTH_SHORT).show()
                    provideHapticFeedback()
                }
            } else {
                Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show()
                provideHapticFeedback()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing QR code", Toast.LENGTH_SHORT).show()
            provideHapticFeedback()
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