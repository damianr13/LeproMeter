package com.bigdict.leprometer.data

import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

class ApplicationInfoStats(val packageName: String, val usageTime: Long) {

    fun getFormattedTimeValue(): String {
        val resultBuilder = StringBuilder()
        var remainder = usageTime;

        val hours = TimeUnit.MILLISECONDS.toHours(remainder)
        remainder -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(remainder)
        remainder -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(remainder)

        if (hours > 0) resultBuilder.append(hours.toString()).append(" h ")
        if (minutes > 0) resultBuilder.append(minutes.toString()).append(" m ")
        if (seconds > 0) resultBuilder.append(seconds.toString()).append(" s")

        return if (resultBuilder.isEmpty()) "Not used" else resultBuilder.toString()
    }
}