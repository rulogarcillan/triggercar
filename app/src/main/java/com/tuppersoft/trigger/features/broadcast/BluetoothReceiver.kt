package com.tuppersoft.trigger.features.broadcast

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tuppersoft.domain.usescases.GetKnowDevicesUseCase
import com.tuppersoft.domain.usescases.GlobalUseCase.None
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.trigger.features.service.Actions
import com.tuppersoft.trigger.features.service.MonitorService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {

    @Inject
    lateinit var useCase: GetKnowDevicesUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            monitoringBluetooth(intent, context)
        }
    }

    private fun monitoringBluetooth(intent: Intent, context: Context) {
        val action = intent.action
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        when {
            BluetoothDevice.ACTION_ACL_CONNECTED == action -> {
                "Device ${device?.name} is now connected".logd()
                CoroutineScope(Dispatchers.IO).launch {
                    useCase.invoke(None()).collect {
                        if (it.filter { device -> device.isSelected }.map { device -> device.mac }
                                .contains(device?.address)) {
                            withContext(Dispatchers.Main) {
                                handleHotSpot(Actions.TURN_ON, context)
                            }
                        }
                    }
                }
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED == action -> {
                "Device ${device?.name} has disconnected".logd()
                CoroutineScope(Dispatchers.IO).launch {
                    useCase.invoke(None()).collect {
                        if (it.map { device -> device.mac }.contains(device?.address)) {
                            withContext(Dispatchers.Main) {
                                handleHotSpot(Actions.TURN_OFF, context)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleHotSpot(action: Actions, context: Context) {
        Intent(context, MonitorService::class.java).also {
            it.action = action.name
            "Starting the service from a BroadcastReceiver".logd()
            context.startForegroundService(it)
        }
    }
}
