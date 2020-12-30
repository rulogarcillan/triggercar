package com.tuppersoft.trigger.core.extension

import android.content.Context
import android.provider.Settings.System

fun Context.canWrite() = System.canWrite(this)
