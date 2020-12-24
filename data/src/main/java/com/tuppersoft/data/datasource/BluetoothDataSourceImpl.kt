package com.tuppersoft.data.datasource

import android.bluetooth.BluetoothAdapter
import com.tuppersoft.data.entities.DeviceEntity
import javax.inject.Inject

class BluetoothDataSourceImpl @Inject constructor(private val bluetoothAdapter: BluetoothAdapter?) :
    BluetoothDataSource {

    override fun getDevices(): List<DeviceEntity> {
        return if (bluetoothAdapter == null) {
            listOf()
        } else {
            bluetoothAdapter.bondedDevices?.let {
                it.map { device ->
                    DeviceEntity(device.name, device.address)
                }
            } ?: listOf()
        }
    }
}
