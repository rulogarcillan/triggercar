package com.tuppersoft.triggercar.features.devices

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference
import com.tuppersoft.triggercar.core.platform.GlobalFragment
import com.tuppersoft.triggercar.databinding.DevicesFragmentBinding
import com.tuppersoft.triggercar.features.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DevicesFragment : GlobalFragment() {

    private val viewModel: MainActivityViewModel by activityViewModels()
    private val devicesViewModel: DevicesViewModel by viewModels()
    private val deviceListAdapter = DeviceListAdapter()

    private var _binding: DevicesFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DevicesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        activity?.registerReceiver(mReceiver, filter)
    }

    private fun init() {
        handleDeviceList()
        devicesViewModel.getDevices(getListDevicesSelecteds())
        deviceListAdapter.setOnclickItemListener {

            val listMacs = getListDevicesSelecteds()
            if (listMacs.contains(it.mac)) {
                listMacs.remove(it.mac)
            } else {
                listMacs.add(it.mac)
            }
            context?.saveSharedPreference(MAC_SELECTED, listMacs.joinToString("-"))

        }
    }

    private fun getListDevicesSelecteds(): MutableList<String> {
        val listString = context?.loadSharedPreference(MAC_SELECTED, "")
        val listMacs = listString?.split("-")?.toMutableList() ?: mutableListOf()
        return listMacs
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

    private fun handleDeviceList() {

        lifecycleScope.launch {
            devicesViewModel.devices.collect { deviceList ->

                binding.rvDevices.adapter = deviceListAdapter.apply {
                    submitList(deviceList)
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

    companion object {

        const val MAC_SELECTED = "MAC_SELECTED"
    }
}
