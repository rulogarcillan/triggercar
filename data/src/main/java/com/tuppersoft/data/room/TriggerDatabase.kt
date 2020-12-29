package com.tuppersoft.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tuppersoft.data.entities.MacEntity

@Database(
    entities = [MacEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TriggerDatabase : RoomDatabase() {

    abstract fun medicineDao(): MacDao
}
