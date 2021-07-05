package com.ifgarces.courseproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf


class PokerRoomsAdapter(private var data :MutableList<PokerRoom>) :
    RecyclerView.Adapter<PokerRoomsAdapter.PokerRoomViewHolder>() {

    override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) : PokerRoomViewHolder {
        return PokerRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_poker_room, parent, false)
        )
    }

    override fun getItemCount() = this.data.count()

    override fun onBindViewHolder(holder : PokerRoomViewHolder, position :Int) = holder.bind(this.data[position], position)

    public fun updateData(data :MutableList<PokerRoom>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    public fun addItem(item :PokerRoom) {
        Logf("[PokerRoomsAdapter] Appending item %s", item)
        this.data.add(item)
        this.notifyItemInserted(this.data.count()-1)
    }

    public fun removeAt(index :Int) {
        Logf("[PokerRoomsAdapter] Removing item %s (at position %d)", this.data[index], index)
        this.data.removeAt(index)
        this.notifyItemRemoved(index)
    }

    public fun getItemAt(index :Int) = this.data[index]

    inner class PokerRoomViewHolder(v :View) : RecyclerView.ViewHolder(v) {
        private val parentView     :View = v
        private val pokerRoom_name :TextView = v.findViewById(R.id.itemRoom_name)
    
        fun bind(item :PokerRoom, position :Int) {
            this.pokerRoom_name.text = item.name
            parentView.setOnClickListener {
                val helper :FragmentActivity = this.parentView.context as FragmentActivity
                helper.intent.putExtra(IntentKeys.POKER_ROOM_NAME, item.name)
                helper.intent.putExtra(IntentKeys.POKER_ROOM_RECYCLER_INDEX, position)

                (this.parentView.context as PlanningActivity).navigator
                    .nativateToRoomDetailsFragment()
            }
        }
    }
}