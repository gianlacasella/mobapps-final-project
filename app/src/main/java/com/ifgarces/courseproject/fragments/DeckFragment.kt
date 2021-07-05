package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.adapters.CardsAdapter


class DeckFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckRecycler :RecyclerView = owner.findViewById(R.id.deck_recyclerview)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) :View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_card_deck, container, false)
        this.UI = FragmentUI(owner = fragView!!)

        this.UI.deckRecycler.layoutManager = GridLayoutManager(
            this.requireActivity().applicationContext,
            3,
            LinearLayoutManager.VERTICAL,
            false
        )
        this.UI.deckRecycler.setHasFixedSize(true)
        this.UI.deckRecycler.adapter = CardsAdapter(
            data = (this.requireActivity() as PlanningActivity).deckCardsViewModel.currentDeckCards
        )

        return fragView
    }
}