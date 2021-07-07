package com.ifgarces.courseproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.utils.IntentKeys


/**
 * Shows the collection of cards in `data`.
 */
class CardsAdapter(private var data :MutableList<Card>) :
    RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    override fun onCreateViewHolder(parent :ViewGroup, viewType :Int): CardsViewHolder {
        return CardsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_deck, parent, false)
        )
    }

    override fun getItemCount() = this.data.count()

    override fun onBindViewHolder(holder :CardsViewHolder, position :Int) =
        holder.bind(this.data[position], position)

    public fun updateData(data :MutableList<Card>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    inner class CardsViewHolder(v :View) : RecyclerView.ViewHolder(v) {
        private val parentView :View = v
        private val cardValueTextView :TextView = v.findViewById(R.id.cardItem_valueTextView)

        fun bind(item :Card, position :Int) {
            this.cardValueTextView.text = item.label.toString()

            this.parentView.setOnClickListener {
                val helper :FragmentActivity = this.parentView.context as FragmentActivity
                helper.intent.putExtra(IntentKeys.CARD_LABEL, item.label)
                helper.intent.putExtra(IntentKeys.CARD_VALUE, item.value)
                (this.parentView.context as PlanningActivity).navigator.navigateToSingleCardFragment()
            }
        }
    }
}