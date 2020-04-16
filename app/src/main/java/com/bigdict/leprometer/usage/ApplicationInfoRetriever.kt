package com.bigdict.leprometer.usage

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import com.bigdict.leprometer.R
import com.bigdict.leprometer.data.ApplicationInfoModel
import com.bigdict.leprometer.data.ApplicationInfoModel.Companion.TYPE_LAZY
import com.bigdict.leprometer.data.ApplicationInfoModel.Companion.TYPE_PRODUCTIVE
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.storage.types.ApplicationType
import com.bigdict.leprometer.storage.types.ApplicationTypePersistenceLayer
import com.bigdict.leprometer.storage.types.OnApplicationTypesRetrieved
import java.util.function.Consumer


class ApplicationInfoRetriever(context: Context) {

    private val mContext = context
    private val mPackageManager = context.packageManager

    private val mApplicationTypePersistenceLayer = ApplicationTypePersistenceLayer(context)

    fun hasApplicationForPackageInstalled(packageName: String): Boolean {
        return try {
            mPackageManager.getApplicationInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getApplicationNameByPackage(packageName: String): String {
        val applicationInfo = mPackageManager.getApplicationInfo(packageName, 0)

        return mPackageManager.getApplicationLabel(applicationInfo).toString()
    }

    fun getApplicationCategoryByPackage(packageName: String): String {
        val applicationInfo = mPackageManager.getApplicationInfo(packageName, 0)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val categoryCharSequence: CharSequence? = ApplicationInfo
                .getCategoryTitle(mContext, applicationInfo.category)

            categoryCharSequence?.toString() ?: ""
        } else {
            "Unknown Category"
        }
    }

    fun getApplicationIcon(packageName: String): Drawable {
        return mPackageManager.getApplicationIcon(packageName)
    }

    fun getArrowImageFor(applicationInfoStats: ApplicationInfoStats): Drawable {
        return when(applicationInfoStats.applicationType) {
            TYPE_LAZY -> mContext.resources.getDrawable(R.drawable.ic_down_red, null)
            TYPE_PRODUCTIVE -> mContext.resources.getDrawable(R.drawable.ic_up_green, null)
            else -> mContext.resources.getDrawable(R.drawable.ic_bar_blue, null)
        }
    }

    fun retrieveAppList(): List<ApplicationInfoModel> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        return mPackageManager.queryIntentActivities(intent, 0)
            .map { it.activityInfo.packageName }
            .map { ApplicationInfoModel(it, getApplicationNameByPackage(it)) }
            .sortedBy { it.applicationName }
    }

    fun retrieveAppListAsync(callback: OnApplicationListRetrieved) {
        mApplicationTypePersistenceLayer.retrieveAllTypesAsync(object: OnApplicationTypesRetrieved {
            override fun onRetrieveCompleted(result: List<ApplicationType>) {
                val allAppsList = retrieveAppList()
                mergeApplicationDataWithType(allAppsList, result)

                callback.onRetrieveCompleted(allAppsList)
            }
        })
    }

    fun mergeApplicationDataWithType(applicationDataList: List<ApplicationInfoModel>,
                                     applicationTypeList: List<ApplicationType>) {
        applicationDataList.forEach { appData ->
            applicationTypeList.firstOrNull { it.packageName == appData.packageName }
            ?.let{ corresponding -> appData.applicationType = corresponding.applicationType}}
    }
}

interface OnApplicationListRetrieved {
    fun onRetrieveCompleted(result: List<ApplicationInfoModel>)
}