package com.bigdict.leprometer.storage.types

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ApplicationType::class], version = 1)
abstract class ApplicationTypeDatabase: RoomDatabase() {
    abstract fun applicationTypeDao(): ApplicationTypesDao
}