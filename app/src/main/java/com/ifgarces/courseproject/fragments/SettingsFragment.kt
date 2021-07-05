package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.utils.Logf


class SettingsFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckTypeSelector :Spinner = owner.findViewById(R.id.settings_deckTypeSpinner)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) :View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_settings, container, false)
        this.UI = FragmentUI(owner = fragView!!)

        (this.requireActivity() as PlanningActivity).let { activity :PlanningActivity ->
            val deckNames :List<String> = activity.deckCardsViewModel.allDecks.map { it.name }

            UI.deckTypeSelector.adapter = ArrayAdapter(
                this.requireContext(), R.layout.spinner_item, deckNames
            )

            UI.deckTypeSelector.setSelection(
                deckNames.indexOf(
                    activity.deckCardsViewModel.currentDeck.name
                )
            )

            UI.deckTypeSelector.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent :AdapterView<*>, view :View, position :Int, id :Long
                ) {
                    val selectedItem :String = parent.getItemAtPosition(position).toString()
                    Logf("[SettingsFragment] Changing user Deck type from %s to %s", activity.deckCardsViewModel.currentDeck.name, selectedItem)
                    activity.deckCardsViewModel
                        .setUserDeck(
                            deck = activity.deckCardsViewModel.allDecks.find { deck :Deck -> deck.name == selectedItem }!!
                        )
                }

                override fun onNothingSelected(parent :AdapterView<*>?) {}
            }
            return fragView
        }
    }
}