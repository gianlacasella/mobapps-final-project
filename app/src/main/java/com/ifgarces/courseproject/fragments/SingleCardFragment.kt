package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.utils.IntentKeys


class SingleCardFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val cardTextView :TextView = owner.findViewById(R.id.cardSingle_cardView)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_card_single, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        val cardLabel :String = this.requireActivity().intent.getStringExtra(IntentKeys.CARD_LABEL)!!
        val cardValue :Double = this.requireActivity().intent.getDoubleExtra(IntentKeys.CARD_VALUE, -1.0)

        this.UI.cardTextView.text = cardLabel

        this.UI.cardTextView.setOnClickListener{
            (context as PlanningActivity).navigator.navigateUp()
        }

        return fragView
    }

}