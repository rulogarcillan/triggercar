package com.tuppersoft.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuppersoft.data.entities.MacEntity

@Dao
interface MacDao {

    @Query("SELECT * FROM macs_actives")
    fun findAll(): List<MacEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMacs(macList: List<MacEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMac(mac: MacEntity)

    @Update
    fun updateMac(mac: MacEntity)

    @Delete
    fun deleteMac(mac: MacEntity)
}
