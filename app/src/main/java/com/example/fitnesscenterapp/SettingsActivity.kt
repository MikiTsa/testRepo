package com.example.fitnesscenterapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

/**
 * SettingsActivity - Manages app settings
 *
 * TASK 4 REQUIREMENT 4:
 * - Provides UI for user preferences.xml
 * - Uses PreferenceFragmentCompat which automatically handles SharedPreferences
 * - Settings are saved to: /data/data/com.example.fitnesscenterapp/shared_prefs/
 *   com.example.fitnesscenterapp_preferences.xml
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Load the settings fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
    }

    /**
     * Handle back button in action bar
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    /**
     * SettingsFragment - Loads preferences.xml from XML
     *
     * PreferenceFragmentCompat automatically:
     * - Creates UI from preferences.xml.xml
     * - Saves changes to SharedPreferences
     * - Loads saved values on startup
     */
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            // Load preferences.xml from XML
            // This XML defines the settings UI and their SharedPreferences keys
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}