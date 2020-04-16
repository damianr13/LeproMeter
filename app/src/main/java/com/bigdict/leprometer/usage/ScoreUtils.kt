package com.bigdict.leprometer.usage

import com.bigdict.leprometer.data.ApplicationInfoModel
import com.bigdict.leprometer.data.ApplicationInfoStats
import java.util.concurrent.TimeUnit

const val MAX_ALLOWED_SCORE = 20000.0

fun computeScore(usageHistory: List<ApplicationInfoStats>): Long {
    return usageHistory.filter { it.applicationType == ApplicationInfoModel.TYPE_LAZY }
        .map{it.usageTime}
        .map{ TimeUnit.MILLISECONDS.toSeconds(it)}
        .sum()
}

fun normalizeScore(scoreValue: Long): Double {
    return when {
        scoreValue <= 0 -> 0.001
        scoreValue > MAX_ALLOWED_SCORE -> 1.0
        else -> scoreValue / MAX_ALLOWED_SCORE
    }
}