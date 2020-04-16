package com.bigdict.leprometer.main_screen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bigdict.leprometer.R
import com.bigdict.leprometer.data.ApplicationInfoStats
import com.bigdict.leprometer.usage.OnApplicationStatsRetrieved
import com.bigdict.leprometer.usage.UsageStatsRetriever
import com.bigdict.leprometer.usage.computeScore
import com.bigdict.leprometer.usage.normalizeScore
import kotlinx.android.synthetic.main.fragment_main_screen.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var view2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view2 = inflater.inflate(R.layout.fragment_main_screen, container, false)

        context?.let{
            val usageStatsRetriever = UsageStatsRetriever(it)
            usageStatsRetriever.retrieveStatsAsync(object: OnApplicationStatsRetrieved {
                override fun onRetrieveCompleted(result: List<ApplicationInfoStats>) {
                    val normalizedScore = normalizeScore(computeScore(result))

                    changeMeeterFill(normalizedScore)
                }
            })
        }
        return view2
    }

    private fun changeMeeterFill(normalizedScore: Double) {
        val imageBitmap = view2.resources.getDrawable(R.drawable.meeter_full, null).toBitmap()
        val imageHeight = (imageBitmap.height * normalizedScore).toInt()
        val croppedBitmap: Bitmap = Bitmap.createBitmap(
            imageBitmap,
            0,
            imageBitmap.height - imageHeight,
            imageBitmap.width,
            imageHeight
        )

        val newImage = BitmapDrawable(view2.resources, croppedBitmap)

        view2.meeter_full.setImageDrawable(newImage)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainScreenFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
            }

    }
}
