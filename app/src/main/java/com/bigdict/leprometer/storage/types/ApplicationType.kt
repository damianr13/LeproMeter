package com.bigdict.leprometer.storage.types

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApplicationType(
    @PrimaryKey val packageName: String,
    @ColumnInfo(name="type") val applicationType: String
)