package com.bigdict.leprometer.advice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bigdict.leprometer.R

/**
 * A simple [Fragment] subclass.
 * Use the [AdviceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdviceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advice, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AdviceFragment.
         */
        @JvmStatic
        fun newInstance() = AdviceFragment()
    }
}
