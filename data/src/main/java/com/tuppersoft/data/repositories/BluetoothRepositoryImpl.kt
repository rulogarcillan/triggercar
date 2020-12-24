package com.tuppersoft.data.repositories;

import com.tuppersoft.data.datasource.BluetoothDataSource
import com.tuppersoft.data.mapper.toDeviceModel
import com.tuppersoft.domain.models.DeviceModel
import com.tuppersoft.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(val bluetoohDataSource: BluetoothDataSource) : BluetoothRepository {

    override suspend fun getKnowDevices(): Flow<List<DeviceModel>> {

        return flow {
            emit(bluetoohDataSource.getDevices().map {
                it.toDeviceModel()
            })
        }
    }
}
