package com.hersonviveros.eliteapartments.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.utils.Constants.Companion.APP_SETTINGS
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DARK_MODE

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyTheme()
    }

    private fun applyTheme() {
        val isDarkMode = isDeviceInDarkMode()
        setTheme(if (isDarkMode) R.style.Theme_Eliteapartments_Dark else R.style.Base_Theme_Eliteapartments)
    }

    private fun isDeviceInDarkMode(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    fun toggleTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val newNightMode = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }

        // Guardar preferencia
        getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(DARK_MODE, newNightMode == AppCompatDelegate.MODE_NIGHT_YES)
            .apply()

        AppCompatDelegate.setDefaultNightMode(newNightMode)

        recreate()
    }
}