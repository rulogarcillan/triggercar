package com.tuppersoft.domain.usescases

import com.tuppersoft.domain.repositories.BluetoothRepository

class DeleteKnowDeviceUseCase constructor(private val repository: BluetoothRepository) :
    GlobalUseCase<Unit, DeleteKnowDeviceUseCase.Param>() {

    data class Param(val mac: String)

    override suspend fun run(params: Param) {
        repository.removeSelectedMac(params.mac)
    }
}
