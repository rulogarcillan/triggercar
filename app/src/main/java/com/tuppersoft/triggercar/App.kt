package com.tuppersoft.triggercar

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(
            loadSharedPreference(
                "THEME_MODE",
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        )

        Stetho.initializeWithDefaults(this)
    }
}

