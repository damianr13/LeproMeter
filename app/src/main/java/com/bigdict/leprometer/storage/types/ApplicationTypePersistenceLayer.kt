package com.bigdict.leprometer.storage.types

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import java.util.function.Consumer


class ApplicationTypePersistenceLayer(context: Context) {
    private val mContext = context

    private val mDatabase: ApplicationTypeDatabase
    private val mDao: ApplicationTypesDao

    init {
        mDatabase = Room.databaseBuilder(mContext,
            ApplicationTypeDatabase::class.java, DATABASE_NAME).build()
        mDao = mDatabase.applicationTypeDao();
    }

    fun getDao(): ApplicationTypesDao{
        return this.mDao
    }

    fun storeType(packageName: String, type: String) {
        AsyncStoreApplicationType(mDao, packageName, type).execute()
    }

    fun retrieveType(packageName: String): String {
        return mDao.getByPackageName(packageName).applicationType
    }

    fun isDatabaseEmpty(): Boolean{
        val answer = CheckDatabaseEmptyAsyncTask(mDao).execute()
        return answer.get()
    }

    fun retrieveAllTypesAsync(onRetrieveComplete: OnApplicationTypesRetrieved) {
        AsyncRetrieveStoredApplicationTypeTask(mDao, onRetrieveComplete).execute()
    }

    companion object {
        private const val DATABASE_NAME = "application_type"
    }
}

private class CheckDatabaseEmptyAsyncTask(val dao: ApplicationTypesDao): AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {
        return dao.getAll().isEmpty()
    }
}

private class AsyncStoreApplicationType(val dao: ApplicationTypesDao,
                                        val packageName: String,
                                        val type: String): AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        dao.deleteByPackageName(packageName)
        dao.insertAll(ApplicationType(packageName, type))
        return null
    }
}

private class AsyncRetrieveStoredApplicationTypeTask(val dao: ApplicationTypesDao,
                                                     val resultConsumer: OnApplicationTypesRetrieved):
    AsyncTask<Void, Void, List<ApplicationType>>() {
    override fun doInBackground(vararg params: Void?): List<ApplicationType> {
        return dao.getAll()
    }

    override fun onPostExecute(result: List<ApplicationType>?) {
        super.onPostExecute(result)
        result?.let {
            resultConsumer.onRetrieveCompleted(it)
        }
    }
}

interface OnApplicationTypesRetrieved {
    fun onRetrieveCompleted(result: List<ApplicationType>)
}
