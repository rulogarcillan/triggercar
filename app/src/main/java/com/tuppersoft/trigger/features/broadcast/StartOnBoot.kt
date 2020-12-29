package com.tuppersoft.trigger.features.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.trigger.features.service.Actions
import com.tuppersoft.trigger.features.service.MonitorService
import com.tuppersoft.trigger.features.service.ServiceState.STARTED
import com.tuppersoft.trigger.features.service.getServiceConfigState

class StartOnBoot : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (intent.action) {
                Intent.ACTION_BOOT_COMPLETED -> {
                    startInBoot(intent, context)
                }
            }
        }
    }

    private fun startInBoot(intent: Intent, context: Context) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED && getServiceConfigState(
                context
            ) == STARTED
        ) {
            Intent(context, MonitorService::class.java).also {
                it.action = Actions.START.name
                "Starting the service from a BroadcastReceiver".logd()
                context.startForegroundService(it)
            }
        }
    }
}
