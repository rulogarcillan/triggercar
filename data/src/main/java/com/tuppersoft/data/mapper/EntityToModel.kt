package com.tuppersoft.data.mapper

import com.tuppersoft.data.entities.DeviceEntity
import com.tuppersoft.domain.models.DeviceModel

fun DeviceEntity.toDeviceModel() = DeviceModel(this.name, this.mac)
