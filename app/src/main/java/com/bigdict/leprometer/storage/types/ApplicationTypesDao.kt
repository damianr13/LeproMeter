package com.bigdict.leprometer.storage.types

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ApplicationTypesDao {
    @Query("SELECT * FROM applicationType")
    fun getAll(): List<ApplicationType>

    @Query("SELECT * FROM applicationType WHERE packageName = :packageName")
    fun getByPackageName(packageName: String): ApplicationType

    @Insert
    fun insertAll(vararg applicationTypes: ApplicationType)

    @Delete
    fun delete(applicationType: ApplicationType)
}