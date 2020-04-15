package com.bigdict.leprometer.stats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigdict.leprometer.R
import com.bigdict.leprometer.usage.ApplicationInfoRetriever
import com.bigdict.leprometer.usage.UsageStatsRetriever
import kotlinx.android.synthetic.main.fragment_application.view.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ApplicationInfoStatsFragment.OnListFragmentInteractionListener] interface.
 */
class ApplicationInfoStatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_application_info_stats_list, container, false)
        context?.let {
            val applicationInfoRetriever = ApplicationInfoRetriever(it)
            val usageStatsRetriever = UsageStatsRetriever(it)

            // Set the adapter
            if (view is RecyclerView) {
                with(view) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = MyApplicationInfoStatsRecyclerViewAdapter(applicationInfoRetriever,
                        usageStatsRetriever.retrieveStats())
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int) = ApplicationInfoStatsFragment()
    }
}
