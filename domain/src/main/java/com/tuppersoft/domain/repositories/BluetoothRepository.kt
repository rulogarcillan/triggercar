package com.tuppersoft.domain.repositories

import com.tuppersoft.domain.models.DeviceModel
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {

    suspend fun getKnowDevices(): Flow<List<DeviceModel>>
}


