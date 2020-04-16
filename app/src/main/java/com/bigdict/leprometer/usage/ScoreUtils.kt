package com.bigdict.leprometer.usage

import com.bigdict.leprometer.data.ApplicationInfoModel
import com.bigdict.leprometer.data.ApplicationInfoStats
import java.util.concurrent.TimeUnit

fun computeScore(usageHistory: List<ApplicationInfoStats>): Long {
    return usageHistory.filter { it.applicationType == ApplicationInfoModel.TYPE_LAZY }
        .map{it.usageTime}
        .map{ TimeUnit.MILLISECONDS.toSeconds(it)}
        .sum()
}