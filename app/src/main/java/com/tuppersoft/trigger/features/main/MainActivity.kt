package com.tuppersoft.trigger.features.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference
import com.tuppersoft.trigger.core.platform.GlobalActivity
import com.tuppersoft.trigger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@AndroidEntryPoint
@RuntimePermissions
class MainActivity : GlobalActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        loadTheme()
        initWithPermissionCheck()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun init() {
        viewModel.permissionAccept(true)
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun onPermissionDenied() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need permission")
            .setMessage("The application need always permission to work")
            .setPositiveButton("Understood") { dialog, which ->
                initWithPermissionCheck()

            }
            .setNegativeButton("Exit") { dialog, which ->
                finish()
            }
            .show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun onPermissionNeverAskAgain() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need permission")
            .setMessage("The application need always permission to work")
            .setPositiveButton("Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Exit") { dialog, which ->
                finish()
            }
            .show()
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
