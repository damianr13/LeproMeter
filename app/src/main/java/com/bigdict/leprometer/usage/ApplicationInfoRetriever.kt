package com.bigdict.leprometer.usage

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.Build

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
}