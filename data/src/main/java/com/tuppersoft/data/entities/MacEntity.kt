package com.tuppersoft.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "macs_actives", primaryKeys = ["mac"])
data class MacEntity(
    @ColumnInfo(name = "mac")
    var mac: String
)
