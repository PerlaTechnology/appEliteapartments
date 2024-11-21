package com.hersonviveros.eliteapartments

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setDefaultNightMode()
    }

    private fun setDefaultNightMode() {
        val isDarkModeEnabled = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            .getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}

