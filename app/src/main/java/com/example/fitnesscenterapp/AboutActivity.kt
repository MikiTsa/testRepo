package com.example.fitnesscenterapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.fitnesscenterapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var app: MyApplication  // TASK 4: Access to MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TASK 4: Get reference to MyApplication
        app = application as MyApplication

        // Existing Task 3 functionality
        displayHtmlText()
        applyCustomFont()

        // TASK 4: Display UUID
        displayUUID()

        // TASK 4: Settings button click listener
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayHtmlText() {
        val htmlString = getString(R.string.app_description_html)
        val formattedText = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
        binding.appDescription.text = formattedText
    }

    private fun applyCustomFont() {
        try {
            val typeface = ResourcesCompat.getFont(this, R.font.custom_font)
            binding.aboutTitle.typeface = typeface
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // TASK 4: Display app UUID
    private fun displayUUID() {
        val uuid = app.getID()
        binding.appUUID.text = "UUID: $uuid"
    }
}