package com.tuppersoft.trigger.features.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.System
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.tuppersoft.trigger.R.string
import com.tuppersoft.trigger.core.extension.canWrite
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
        initWithPermissionCheck()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun init() {
        if (!canWrite()) {
            settingPermission()
        } else {
            viewModel.permissionAccept(true)
        }
    }

    private fun settingPermission() {
        if (!System.canWrite(applicationContext)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:$packageName"))
            startActivityForResult(intent, MY_PERMISSIONS_MANAGE_WRITE_SETTINGS)
        }
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun onPermissionDenied() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(string.need_permission))
            .setMessage(getString(string.need_permission_explained))
            .setPositiveButton(getString(string.understood)) { dialog, which ->
                initWithPermissionCheck()
            }
            .setNegativeButton(getString(string.exit)) { dialog, which ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    fun onPermissionNeverAskAgain() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(string.need_permission)
            .setMessage(string.need_permission_explained)
            .setPositiveButton(getString(string.settings)) { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(string.exit) { dialog, which ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_PERMISSIONS_MANAGE_WRITE_SETTINGS) {
            if (canWrite()) {
                viewModel.permissionAccept(true)
            } else {
                settingPermission()
            }
        }
    }

    companion object {

        const val MY_PERMISSIONS_MANAGE_WRITE_SETTINGS = 100
    }
}
