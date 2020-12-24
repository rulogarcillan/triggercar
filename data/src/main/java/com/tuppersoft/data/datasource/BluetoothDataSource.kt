package com.tuppersoft.data.datasource

import com.tuppersoft.data.entities.DeviceEntity

interface BluetoothDataSource  {

    fun getDevices(): List<DeviceEntity>

}
