package com.tuppersoft.domain.usescases

import com.tuppersoft.domain.models.DeviceModel
import com.tuppersoft.domain.repositories.BluetoothRepository
import com.tuppersoft.domain.usescases.GlobalUseCase.None
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetKnowDevicesUseCase constructor(private val repository: BluetoothRepository) :
    GlobalUseCase<Flow<List<DeviceModel>>, None>() {

    override suspend fun run(params: None): Flow<List<DeviceModel>> {
        val flow = repository.getKnowDevices()
        val listMac = repository.getSelectedMacs()

        return flow.map {
            it.forEach { device -> device.isSelected = listMac.contains(device.mac) }
            it
        }
    }
}
