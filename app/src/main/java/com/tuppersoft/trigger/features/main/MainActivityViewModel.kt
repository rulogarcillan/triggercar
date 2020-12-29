package com.tuppersoft.trigger.features.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Raúl Rodríguez Concepción on 22/05/2020
 * raulrcs@gmail.com
 */
class MainActivityViewModel @ViewModelInject constructor() : ViewModel() {

    private val _permissionAccept: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val permissionAccept: StateFlow<Boolean> get() = _permissionAccept

    fun permissionAccept(flag: Boolean) {
        _permissionAccept.value = flag
    }
}

