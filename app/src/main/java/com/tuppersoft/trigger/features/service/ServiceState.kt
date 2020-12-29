package com.tuppersoft.trigger.features.service

import android.content.Context
import com.tuppersoft.skizo.android.core.extension.loadSharedPreference
import com.tuppersoft.skizo.android.core.extension.saveSharedPreference

enum class ServiceState {
    STARTED,
    STOPPED,
}

private const val CONFIG_SERVICE_STATE = "CONFIG_SERVICE_STATE"


fun setServiceConfigState(context: Context, state: ServiceState) {
    context.saveSharedPreference(CONFIG_SERVICE_STATE, state.name)
}

fun getServiceConfigState(context: Context): ServiceState {
    val value: String = context.loadSharedPreference(CONFIG_SERVICE_STATE, ServiceState.STOPPED.name)
    return ServiceState.valueOf(value)
}
