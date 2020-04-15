package com.bigdict.leprometer.storage.types

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import androidx.room.Room
import java.lang.ref.WeakReference


class ApplicationTypePersistenceLayer(context: Context) {
    private val mContext = context

    private val mDatabase: ApplicationTypeDatabase
    private val mDao: ApplicationTypesDao

    init {
        mDatabase = Room.databaseBuilder(mContext,
            ApplicationTypeDatabase::class.java, DATABASE_NAME).build()
        mDao = mDatabase.applicationTypeDao();
    }

    open fun getDao(): ApplicationTypesDao{
        return this.mDao
    }

    fun storeType(packageName: String, type: String) {
        mDao.insertAll(ApplicationType(packageName, type))
    }

    fun retrieveType(packageName: String): String {
        return mDao.getByPackageName(packageName).applicationType
    }

    fun isDatabaseEmpty(): Boolean{
        val answer = AsyncDatabaseAccess(this.mContext).execute()
        return answer.get()
    }

    companion object {
        private const val DATABASE_NAME = "application_type"
    }
}

private class AsyncDatabaseAccess(val context: Context): AsyncTask<Void, Void, Boolean>() {
    override fun doInBackground(vararg params: Void?): Boolean {
        val data = ApplicationTypePersistenceLayer(this.context).getDao().getAll()
        if(data.isEmpty()){
            return true
        }
        return false

    }
}