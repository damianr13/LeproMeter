package com.bigdict.leprometer.usage

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import com.bigdict.leprometer.data.ApplicationInfoStats
import java.util.*

class UsageStatsRetriever(context: Context) {

    private val mContext = context
    private val mApplicationInfoRetriever = ApplicationInfoRetriever(context)

    fun retrieveStats(): List<ApplicationInfoStats> {
        val manager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val now = Calendar.getInstance()
        val startOfDay = Calendar.getInstance()
        startOfDay.set(
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 1,
            0, 0, 0
        )

        val usageStats = manager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            startOfDay.timeInMillis, now.timeInMillis
        )

        return groupSimilar(usageStats)
    }

    private fun groupSimilar(usageStats: List<UsageStats>): List<ApplicationInfoStats> {
        return usageStats.asSequence().groupBy { it.packageName }
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