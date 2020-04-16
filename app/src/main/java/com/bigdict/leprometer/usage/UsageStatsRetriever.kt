package com.bigdict.leprometer.usage

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.storage.types.ApplicationType
import com.bigdict.leprometer.storage.types.ApplicationTypePersistenceLayer
import com.bigdict.leprometer.storage.types.OnApplicationTypesRetrieved
import java.util.*

class UsageStatsRetriever(context: Context) {

    private val mContext = context
    private val mApplicationInfoRetriever = ApplicationInfoRetriever(context)

    private val mApplicationTypePersistenceLayer = ApplicationTypePersistenceLayer(context)

    fun retrieveStats(): List<ApplicationInfoStats> {
        val manager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val now = Calendar.getInstance()
        val startOfDay = Calendar.getInstance()
        startOfDay.set(
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
            0, 0, 1
        )

        Log.d("retrieveStats", "${startOfDay.timeInMillis}")
        Log.d("retrieveStats", "${now.timeInMillis}")

        val usageStats = manager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            startOfDay.timeInMillis, now.timeInMillis
        ).filter { it.lastTimeStamp > startOfDay.timeInMillis || it.firstTimeStamp > startOfDay.timeInMillis }

        val fullAppListPackages = mApplicationInfoRetriever.retrieveAppList()
            .map { it.packageName }
        val usedApps = groupSimilar(usageStats)
            .filter { fullAppListPackages.contains(it.packageName) }
            .toList()

        val result = ArrayList<ApplicationInfoStats>()
        result.addAll(usedApps)

        val usedAppsPackages = usedApps.map { it.packageName }
        fullAppListPackages.filter { !usedAppsPackages.contains(it)}.forEach {
            result.add(ApplicationInfoStats(it, 0, mApplicationInfoRetriever.getApplicationNameByPackage(it)))
        }

        return result
    }

    fun retrieveStatsAsync(callback: OnApplicationStatsRetrieved) {
        mApplicationTypePersistenceLayer.retrieveAllTypesAsync(object: OnApplicationTypesRetrieved {
            override fun onRetrieveCompleted(result: List<ApplicationType>) {
                val allAppsList = retrieveStats()
                mApplicationInfoRetriever.mergeApplicationDataWithType(allAppsList, result)

                callback.onRetrieveCompleted(allAppsList)
            }
        })
    }

    private fun groupSimilar(usageStats: List<UsageStats>): List<ApplicationInfoStats> {
        return usageStats.asSequence().groupBy { it.packageName }
            .filter { mApplicationInfoRetriever.hasApplicationForPackageInstalled(it.key) }
            .map { createApplicationInfoFromEventGroup(it.value) }
            .filterNotNull()
            .toList()
            .sortedByDescending { it.usageTime }
            .toList()
    }

    private fun createApplicationInfoFromEventGroup(usageStats: List<UsageStats>): ApplicationInfoStats? {
        if (usageStats.isEmpty()) {
            return null
        }

        val usageTime = usageStats.map { it.totalTimeInForeground }.sum()

        return ApplicationInfoStats(
            usageStats.first().packageName,
            usageTime,
            mApplicationInfoRetriever.getApplicationNameByPackage(usageStats.first().packageName)
        )
    }
}

interface OnApplicationStatsRetrieved {
    fun onRetrieveCompleted(result: List<ApplicationInfoStats>)
}