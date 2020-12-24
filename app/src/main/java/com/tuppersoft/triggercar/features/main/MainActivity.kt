package com.tuppersoft.triggercar.features.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference
import com.tuppersoft.triggercar.core.platform.GlobalActivity
import com.tuppersoft.triggercar.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : GlobalActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        loadTheme()
    }

    private fun changeTheme() {
        if (this.loadSharedPreference(
                "THEME_MODE",
                delegate.localNightMode
            ) == AppCompatDelegate.MODE_NIGHT_YES
        ) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
        saveSharedPreference("THEME_MODE", delegate.localNightMode)
    }

    private fun loadTheme() {
        delegate.localNightMode = loadSharedPreference("THEME_MODE", delegate.localNightMode)
    }
}
