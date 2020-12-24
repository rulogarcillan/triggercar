package com.tuppersoft.triggercar.features.devices

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuppersoft.domain.usescases.GetKnowDevicesUseCase
import com.tuppersoft.domain.usescases.GlobalUseCase.None
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Raúl Rodríguez Concepción on 22/05/2020.
 * raulrcs@gmail.com
 */
class DevicesViewModel @ViewModelInject constructor(private val getKnowDevicesUseCase: GetKnowDevicesUseCase) :
    ViewModel() {

    private val _devices: MutableStateFlow<List<DeviceNav>> = MutableStateFlow(listOf())
    val devices: StateFlow<List<DeviceNav>> get() = _devices

    fun getDevices(getListDevicesSelecteds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {

            getKnowDevicesUseCase.invoke(None()).collect {

                _devices.value = it.map { device ->
                    DeviceNav(
                        device.name,
                        device.mac,
                        getListDevicesSelecteds.contains(device.mac)
                    )
                }

            }
        }
    }
}
