package com.example.fitnesscenterapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesscenterapp.databinding.ActivityAboutBinding

/**
 * AboutActivity - Shows application information
 *
 * CHANGES FOR TASK 4:
 * - Now displays app UUID from MyApplication
 */
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    // TASK 4: Access to MyApplication for UUID
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TASK 4: Get reference to MyApplication
        app = application as MyApplication

        // Display app information
        displayAppInfo()
    }

    /**
     * Display complete app information including UUID
     * TASK 4 REQUIREMENT: Display UUID from MyApplication
     */
    private fun displayAppInfo() {
        val appName = getString(R.string.app_name)
        val appDescription = getString(R.string.app_description)
        val appVersion = "4.0"
        val author = "MikiTsa"

        // TASK 4: Get UUID from MyApplication
        val appUUID = app.getID()

        // Build the info text
        val appInfo = buildString {
            append("═══════════════════════════\n")
            append("📱 APPLICATION INFO\n")
            append("═══════════════════════════\n\n")
            append("App Name: $appName\n\n")
            append("Version: $appVersion\n\n")
            append("Description:\n$appDescription\n\n")
            append("Author: $author\n\n")
            append("───────────────────────────\n")
            append("🔑 APP UUID (Task 4):\n")
            append("$appUUID\n")
            append("───────────────────────────\n\n")
            append("This UUID is generated once on\n")
            append("first app installation and stored\n")
            append("in SharedPreferences.\n")
        }

        // Display in TextView
        binding.tvAppInfo.text = appInfo
    }
}