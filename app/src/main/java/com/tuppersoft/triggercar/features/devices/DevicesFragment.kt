package com.tuppersoft.triggercar.main

import android.Manifest.permission
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.triggercar.core.platform.GlobalFragment
import com.tuppersoft.triggercar.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : GlobalFragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        activity?.registerReceiver(mReceiver, filter)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            when {
                BluetoothDevice.ACTION_FOUND == action -> {
                    "Device found".logd()
                }
                BluetoothDevice.ACTION_ACL_CONNECTED == action -> {
                    "Device ${device?.name} is now connected".logd()
                    turnOn()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action -> {
                    "Done searching".logd()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED == action -> {
                    "Device is about to disconnect".logd()
                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {
                    "Device ${device?.name} has disconnected".logd()
                    turnOff()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private var mReservation: LocalOnlyHotspotReservation? = null

    @RequiresApi(api = VERSION_CODES.O)
    private fun turnOn() {
        val manager = activity?.applicationContext?.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            "no permision".logd()
            return
        }
        manager.startLocalOnlyHotspot(object : LocalOnlyHotspotCallback() {
            override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                super.onStarted(reservation)
                "Wifi Hotspot is on now".logd()
                mReservation = reservation
            }

            override fun onStopped() {
                super.onStopped()
                "onStopped: ".logd()
            }

            override fun onFailed(reason: Int) {
                super.onFailed(reason)
                "onFailed".logd()
            }
        }, Handler())
    }

    private fun turnOff() {
        mReservation?.close()
    }
}
