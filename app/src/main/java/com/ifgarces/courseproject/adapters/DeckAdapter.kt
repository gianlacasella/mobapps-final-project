package com.ifgarces.courseproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.utils.IntentKeys


class DeckAdapter(var context: Context, var deck : Deck) : RecyclerView.Adapter<DeckAdapter.ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder{
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_card_deck, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int{
        return deck.getCardsCount()
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int){
        val cards = deck.cards
        cards.forEach{
            println(it.label)
        }
        val card = cards.get(position)
        holder.cardsTexts.text = card.label
        holder.cardsTexts.setOnClickListener {
            val helper :FragmentActivity = holder.parentView.context as FragmentActivity
            helper.intent.putExtra(IntentKeys.CARD_LABEL, card.label)
            helper.intent.putExtra(IntentKeys.CARD_VALUE, card.value)
            (holder.parentView.context as PlanningActivity).navigator.navigateToSingleCardFragment()
        }
    }

    class ItemHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val parentView :View = itemView
        val cardsTexts :TextView = itemView.findViewById(R.id.card_text)
    }
}