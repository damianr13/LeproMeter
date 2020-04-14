package com.bigdict.leprometer.usage

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.*


class UsageStatsRetriever(context: Context) {

    private val mContext = context;

    public fun retrieveStats(): String {
        val manager = mContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val now = Calendar.getInstance()
        val startOfDay = Calendar.getInstance()
        startOfDay.set(
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH) - 1,
            0, 0, 0)

        val usageStats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,
            startOfDay.timeInMillis, now.timeInMillis)

        val resultBuilder = StringBuilder()

        usageStats.forEach{
            resultBuilder.append(it.packageName)
                .append(" ")
                .append(it.totalTimeInForeground.toString())
                .append("\n")
        }

        return resultBuilder.toString()
    }

    companion object {
        private val TAG = UsageStatsRetriever::class.qualifiedName
    }
}