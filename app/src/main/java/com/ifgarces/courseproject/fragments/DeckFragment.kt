package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.adapters.DeckAdapter
import com.ifgarces.courseproject.models.DataMaster


class DeckFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckRecycler :RecyclerView = owner.findViewById(R.id.deck_recyclerview)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_card_deck, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        val layoutManager = GridLayoutManager(context, 6)
        val adapter = DeckAdapter(
                this.requireActivity().applicationContext, DataMaster.getUserCardDeck()
        )

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapter.itemCount
                val extra: Int
                extra = item % 3
                if (extra == 0) {
                    return 2
                }
                if (item - (position + 1) < extra) {
                    return (6  / extra) as Int

                } else {
                    return 2
                }
            }
        }

        this.UI.deckRecycler.layoutManager = layoutManager

        this.UI.deckRecycler.setHasFixedSize(true)
        this.UI.deckRecycler.adapter = adapter

        return fragView
    }
}