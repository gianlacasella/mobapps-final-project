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
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.enums.DeckType
import com.ifgarces.courseproject.models.DataMaster


class SettingsFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckTypeSelector :Spinner = owner.findViewById(R.id.settings_deckTypeSpinner)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_settings, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        val deckMapping :Map<String, DeckType> = mapOf(
            DeckType.Standard.toString()  to DeckType.Standard,
            DeckType.Hours.toString()     to DeckType.Hours,
            DeckType.Fibonacci.toString() to DeckType.Fibonacci,
            DeckType.T_Shirt.toString()   to DeckType.T_Shirt
        )

        val orderedKeys :List<String> = deckMapping.keys.toList().sorted() // ordering, otherwise order is not guaranteed because it's a map, so we later can always know the DeckType from the index
        this.UI.deckTypeSelector.adapter = ArrayAdapter(
            this.requireContext(), R.layout.spinner_item, orderedKeys
        )

        this.UI.deckTypeSelector.setSelection(
            orderedKeys.indexOf(DataMaster.getUserCardDeck().type.toString())
        )

        this.UI.deckTypeSelector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View, position: Int, id: Long
            ) {
                val selectedItem :String = parent.getItemAtPosition(position).toString()
                val selectedDeckType :DeckType = deckMapping[selectedItem]!!
                DataMaster.setDeckType(selectedDeckType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return fragView
    }
}