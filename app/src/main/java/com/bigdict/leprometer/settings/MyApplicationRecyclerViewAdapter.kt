package com.bigdict.leprometer.settings


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bigdict.leprometer.R
import com.bigdict.leprometer.data.ApplicationInfoModel
import com.bigdict.leprometer.dummy.DummyContent.DummyItem
import com.bigdict.leprometer.settings.SettingsFragment.OnListFragmentInteractionListener
import com.bigdict.leprometer.storage.types.ApplicationTypePersistenceLayer
import com.bigdict.leprometer.usage.ApplicationInfoRetriever
import kotlinx.android.synthetic.main.fragment_application.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyApplicationRecyclerViewAdapter(
    private val mApplicationInfoRetriever: ApplicationInfoRetriever,
    private val mValues: List<ApplicationInfoModel>,
    private val mAppPersistenceLayer: ApplicationTypePersistenceLayer
) : RecyclerView.Adapter<MyApplicationRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_application, parent, false)

        return ViewHolder(view)
    }

    private fun markHighlighted(view: View) {
        view.setBackgroundColor(view.resources.getColor(R.color.colorSelectedHighlight, null))
    }

    private fun unmarkedHighlighted(view: View) {
        view.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun onApplicationTypeSelected(item: ApplicationInfoModel, holder: ViewHolder, type: String) {
        item.applicationType = type
        mAppPersistenceLayer.storeType(item.packageName, item.applicationType)

        synchronizeHolderWithData(holder, item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        synchronizeHolderWithData(holder, item)

        holder.mLazyImageView.setOnClickListener(View.OnClickListener {
            onApplicationTypeSelected(item, holder, ApplicationInfoModel.TYPE_LAZY)
        })

        holder.mProductiveView.setOnClickListener(View.OnClickListener {
            onApplicationTypeSelected(item, holder, ApplicationInfoModel.TYPE_PRODUCTIVE)
        })

        with(holder.mView) {
            tag = item
        }
    }

    private fun synchronizeHolderWithData(holder: ViewHolder, item: ApplicationInfoModel) {
        holder.mIdView.text = item.applicationName
        holder.mAppIconImageView.setImageDrawable(mApplicationInfoRetriever.getApplicationIcon(item.packageName))

        // unmarked by default
        unmarkedHighlighted(holder.mProductiveView)
        unmarkedHighlighted(holder.mLazyImageView)

        if (item.applicationType == ApplicationInfoModel.TYPE_LAZY) {
            markHighlighted(holder.mLazyImageView)
        }

        if (item.applicationType == ApplicationInfoModel.TYPE_PRODUCTIVE) {
            markHighlighted(holder.mProductiveView)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mAppIconImageView: ImageView = mView.app_icon
        val mLazyImageView: ImageView = mView.lazy_app
        val mProductiveView: ImageView = mView.productive_app
    }
}
