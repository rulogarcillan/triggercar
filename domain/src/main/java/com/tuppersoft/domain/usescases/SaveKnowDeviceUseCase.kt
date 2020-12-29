package com.tuppersoft.domain.usescases

import com.tuppersoft.domain.repositories.BluetoothRepository

class SaveKnowDeviceUseCase constructor(private val repository: BluetoothRepository) :
    GlobalUseCase<Unit, SaveKnowDeviceUseCase.Param>() {

    data class Param(val mac: String)

    override suspend fun run(params: Param) {
        repository.saveSelectedMac(params.mac)
    }
}
