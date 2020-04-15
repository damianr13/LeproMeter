package com.bigdict.leprometer.settings

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bigdict.leprometer.R


import com.bigdict.leprometer.settings.SettingsFragment.OnListFragmentInteractionListener
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.dummy.DummyContent.DummyItem
import com.bigdict.leprometer.usage.ApplicationInfoRetriever

import kotlinx.android.synthetic.main.fragment_application.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyApplicationRecyclerViewAdapter(
    private val mApplicationInfoRetriever: ApplicationInfoRetriever,
    private val mValues: List<ApplicationInfoStats>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyApplicationRecyclerViewAdapter.ViewHolder>() {


    lateinit var productiveImageClick: ImageView
    lateinit var lazyImageClick: ImageView

    init {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_application, parent, false)
        lazyImageClick = view.lazy_app
        productiveImageClick = view.productive_app

        lazyImageClick.setOnClickListener(View.OnClickListener {
            Log.d("Settings", "am dat click pe lazzy")

        })
        productiveImageClick.setOnClickListener(View.OnClickListener {
            Log.d("Settings", "am dat click pe productive")
        })
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.applicationName
        holder.mAppIconImageView.setImageDrawable(mApplicationInfoRetriever.getApplicationIcon(item.packageName))

        with(holder.mView) {
            tag = item
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mAppIconImageView = mView.app_icon

    }
}
