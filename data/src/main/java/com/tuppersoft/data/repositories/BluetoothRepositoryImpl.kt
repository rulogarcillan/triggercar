package com.tuppersoft.data.repositories;

import com.tuppersoft.data.datasource.BluetoothDataSource
import com.tuppersoft.data.entities.MacEntity
import com.tuppersoft.data.mapper.toDeviceModel
import com.tuppersoft.data.room.TriggerDatabase
import com.tuppersoft.domain.models.DeviceModel
import com.tuppersoft.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    val bluetoohDataSource: BluetoothDataSource,
    val bbddDataSource: TriggerDatabase
) : BluetoothRepository {

    override suspend fun getKnowDevices(): Flow<List<DeviceModel>> {

        return flow {
            emit(bluetoohDataSource.getDevices().map {
                it.toDeviceModel()
            })
        }
    }

    override suspend fun getSelectedMacs(): List<String> {
        return bbddDataSource.medicineDao().findAll().map { it.mac }
    }

    override suspend fun removeSelectedMac(mac: String) {
        bbddDataSource.medicineDao().deleteMac(MacEntity(mac))
    }

    override suspend fun saveSelectedMac(mac: String) {
        bbddDataSource.medicineDao().insertMac(MacEntity(mac))
    }
}
