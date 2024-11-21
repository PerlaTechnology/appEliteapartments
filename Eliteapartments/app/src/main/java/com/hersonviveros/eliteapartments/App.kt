package com.hersonviveros.eliteapartments

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.hersonviveros.eliteapartments.utils.Constants.Companion.APP_SETTINGS
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DARK_MODE
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setDefaultNightMode()
    }

    private fun setDefaultNightMode() {
        val isDarkModeEnabled = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
            .getBoolean(DARK_MODE, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}

