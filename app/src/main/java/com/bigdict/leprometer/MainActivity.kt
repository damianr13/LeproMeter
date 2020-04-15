package com.bigdict.leprometer

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bigdict.leprometer.dummy.DummyContent
import com.bigdict.leprometer.settings.SettingsFragment
import com.bigdict.leprometer.stats.ApplicationInfoStatsFragment
import com.bigdict.leprometer.storage.types.ApplicationTypePersistenceLayer
import com.bigdict.leprometer.usage.UsageStatsRetriever
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SettingsFragment.OnListFragmentInteractionListener {

    private lateinit var mUsageStatsRetriever: UsageStatsRetriever

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_settings -> {
                swapMainFragment(SettingsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_statistics -> {
                swapMainFragment(ApplicationInfoStatsFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    private fun swapMainFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
            .commit()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        mUsageStatsRetriever = UsageStatsRetriever(this)

        if (ensureAccessGranted()) {
            val text = mUsageStatsRetriever.retrieveStats()
        }
        if (savedInstanceState == null) {
            if (ApplicationTypePersistenceLayer(this).isDatabaseEmpty()){
                val fragment = SettingsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
            }
            val fragment = SettingsFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
    }


    private fun ensureAccessGranted(): Boolean {
        if (hasAccessGranted()) {
            return true
        }

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.PACKAGE_USAGE_STATS),
            PACKAGE_USAGE_STATS_REQUEST)


        if (hasAccessGranted()) {
            return true;
        }

        val intent = Intent(
            Settings.ACTION_USAGE_ACCESS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        return hasAccessGranted()
    }

    private fun hasAccessGranted(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )

        return if (mode == AppOpsManager.MODE_DEFAULT) {
            checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    companion object {
        private const val PACKAGE_USAGE_STATS_REQUEST = 101;
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }
}
