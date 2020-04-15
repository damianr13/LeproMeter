package com.bigdict.leprometer.usage

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Build
import com.bigdict.leprometer.R
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.data.ApplicationInfo.Companion.TYPE_LAZY
import com.bigdict.leprometer.data.ApplicationInfo.Companion.TYPE_PRODUCTIVE

class ApplicationInfoRetriever(context: Context) {

    private val mContext = context
    private val mPackageManager = context.packageManager

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
}