package com.bigdict.leprometer.storage.types

import android.content.Context
import androidx.room.Room

class ApplicationTypePersistenceLayer(context: Context) {
    private val mContext = context

    private val mDatabase: ApplicationTypeDatabase
    private val mDao: ApplicationTypesDao

    init {
        mDatabase = Room.databaseBuilder(mContext,
            ApplicationTypeDatabase::class.java, DATABASE_NAME).build()
        mDao = mDatabase.applicationTypeDao();
    }

    fun storeType(packageName: String, type: String) {
        mDao.insertAll(ApplicationType(packageName, type))
    }

    fun retrieveType(packageName: String): String {
        return mDao.getByPackageName(packageName).applicationType
    }

    companion object {
        private const val DATABASE_NAME = "application_type"
    }
}