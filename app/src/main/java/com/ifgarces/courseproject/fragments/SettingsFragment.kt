package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog


class SettingsFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckTypeSelector :Spinner = owner.findViewById(R.id.settings_deckTypeSpinner)
        val titleText        :TextView = owner.findViewById(R.id.settings_title)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) :View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_settings, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        (this.requireActivity() as PlanningActivity).let { planningActivity :PlanningActivity ->
            if (planningActivity.selectedJoinedPokerRoom == null) {
                planningActivity.infoDialog(
                    title = "No selected PokerRoom",
                    message = "Please select a PokerRoom you are member before managing it",
                    onDismiss = {
                        //planningActivity.navigator.navigateUp()
                    },
                    icon = null
                )
                return null
            }

            UI.titleText.text = planningActivity.selectedJoinedPokerRoom!!.pokerRoomName

            val deckNames :List<String> = planningActivity.deckCardsViewModel.allDecks.map { it.name }

            UI.deckTypeSelector.adapter = ArrayAdapter(
                this.requireContext(), R.layout.spinner_item, deckNames
            )

            UI.deckTypeSelector.setSelection(
                deckNames.indexOf(
                    planningActivity.deckCardsViewModel.currentDeck.name
                )
            )

            UI.deckTypeSelector.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent :AdapterView<*>, view :View, position :Int, id :Long
                ) {
                    val selectedItem :String = parent.getItemAtPosition(position).toString()
                    Logf("[SettingsFragment] Changing user Deck type from %s to %s", planningActivity.deckCardsViewModel.currentDeck.name, selectedItem)
                    planningActivity.deckCardsViewModel
                        .setUserDeck(
                            deck = planningActivity.deckCardsViewModel.allDecks.find { deck :Deck -> deck.name == selectedItem }!!
                        )
                }

                override fun onNothingSelected(parent :AdapterView<*>?) {}
            }
            return fragView
        }
    }
}