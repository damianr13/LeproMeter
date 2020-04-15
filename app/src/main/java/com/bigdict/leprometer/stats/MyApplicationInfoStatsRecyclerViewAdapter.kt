package com.bigdict.leprometer.stats

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bigdict.leprometer.R
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.usage.ApplicationInfoRetriever

import kotlinx.android.synthetic.main.fragment_application_info_stats.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyApplicationInfoStatsRecyclerViewAdapter(
    private val mApplicationInfoRetriever: ApplicationInfoRetriever,
    private val mValues: List<ApplicationInfoStats>
) : RecyclerView.Adapter<MyApplicationInfoStatsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_application_info_stats, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mAppIconImageView.setImageDrawable(
            mApplicationInfoRetriever.getApplicationIcon(item.packageName))
        holder.mAppNameTextView.text = item.applicationName
        holder.mUsageTextView.text = item.getFormattedTimeValue()
        holder.mArrowImageView.setImageDrawable(
            mApplicationInfoRetriever.getArrowImageFor(item))

        with(holder.mView) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mAppIconImageView: ImageView = mView.iv_app_icon
        val mAppNameTextView: TextView = mView.tv_app_name
        val mUsageTextView: TextView = mView.tv_app_usage
        val mArrowImageView: ImageView = mView.iv_app_arrow
    }
}
