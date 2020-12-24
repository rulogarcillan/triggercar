package com.tuppersoft.domain.usescases

import com.tuppersoft.domain.models.DeviceModel
import com.tuppersoft.domain.repositories.BluetoothRepository
import com.tuppersoft.domain.usescases.GlobalUseCase.None
import kotlinx.coroutines.flow.Flow

class GetKnowDevicesUseCase constructor(private val repository: BluetoothRepository) :
    GlobalUseCase<Flow<List<DeviceModel>>, None>() {

    override suspend fun run(params: None): Flow<List<DeviceModel>> {
        return repository.getKnowDevices()
    }
}
