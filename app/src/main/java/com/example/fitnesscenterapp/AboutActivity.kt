package com.example.fitnesscenterapp

import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.fitnesscenterapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayHtmlText()
        applyCustomFont()
    }

    private fun displayHtmlText() {
        val htmlString = getString(R.string.app_description_html)

        val formattedText =
            Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)

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
}