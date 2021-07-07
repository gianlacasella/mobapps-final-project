package com.ifgarces.courseproject.adapters

import android.graphics.Color
import android.graphics.ColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog


class PokerRoomsAdapter(private var data :MutableList<PokerRoom>) :
    RecyclerView.Adapter<PokerRoomsAdapter.PokerRoomViewHolder>() {

    override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) : PokerRoomViewHolder {
        return PokerRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_poker_room, parent, false)
        )
    }

    override fun getItemCount() = this.data.count()

    override fun onBindViewHolder(holder :PokerRoomViewHolder, position :Int) = holder.bind(this.data[position], position)

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
        private val parentView       :View = v
        private val pokerRoom_name   :TextView = v.findViewById(R.id.pokerRoomItem_name)
        private val pokerRoom_status :ImageView = v.findViewById(R.id.pokerRoomItem_statusImage)
    
        fun bind(item :PokerRoom, position :Int) {
            (this.parentView.context as PlanningActivity).let { planningActivity: PlanningActivity ->
                this.pokerRoom_name.text = item.name

                // Setting online/offline status image, depending on the presence of the online API
                // metadata of the PokerRoom
                if (item.isOnline()) {
                    this.pokerRoom_status.setImageResource(R.drawable.online_icon)
                    this.pokerRoom_status.setColorFilter(Color.argb(255, 24, 134, 247))
                } else {
                    this.pokerRoom_status.setImageResource(R.drawable.offline_icon)
                    //this.pokerRoom_status.setColorFilter(Color.RED)
                }

                // If the current PokerRoom is the selected one, it is noticed in the UI by
                // colorizing the recycler element
                if (item.name == planningActivity.selectedJoinedPokerRoom?.pokerRoomName) {
                    this.parentView.background.setTint(this.parentView.context.resources.getColor(R.color.selectedPokerRoomTint))
                }

                this.parentView.setOnClickListener {
                    val helper :FragmentActivity = this.parentView.context as FragmentActivity
                    helper.intent.putExtra(IntentKeys.POKER_ROOM_NAME, item.name)
                    helper.intent.putExtra(IntentKeys.POKER_ROOM_RECYCLER_INDEX, position)

                    planningActivity.navigator.nativateToRoomDetailsFragment()
                }

                this.pokerRoom_status.setOnClickListener {
                    this.parentView.context.infoDialog(
                        title = item.name,
                        message = "Status: %s".format(if (item.isOnline()) "ONLINE" else "OFFLINE")
                    )
                }
            }
        }
    }
}