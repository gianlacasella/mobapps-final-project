package com.ifgarces.courseproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PokerUsersAdapter(private var data :MutableList<PokerRoomsApiClasses.RoomMember>) :
    RecyclerView.Adapter<PokerUsersAdapter.PokerUserVH>() {

    override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :PokerUserVH {
        return PokerUserVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_room_user, parent, false)
        )
    }

    override fun getItemCount() = this.data.count()

    override fun onBindViewHolder(holder :PokerUserVH, position :Int) =
        holder.bind(this.data[position], position)

    public fun updateData(data :MutableList<PokerRoomsApiClasses.RoomMember>) {
        this.data = data
        this.notifyDataSetChanged()
    }

    inner class PokerUserVH(v :View) : RecyclerView.ViewHolder(v) {
        private val parentView :View = v
        private val username   :TextView = v.findViewById(R.id.userItem_name)
        private val status     :TextView = v.findViewById(R.id.userItem_status)
        private val address    :TextView = v.findViewById(R.id.userItem_address)

        fun bind(item :PokerRoomsApiClasses.RoomMember, position :Int) {
            val username_val    :String = item.username
            val lat             :String = item.location.lat
            val long            :String = item.location.long
            val timestamp       :String = item.location.timestamp

            this.username.text = username_val
            this.status.text = "Last known connection: ${timestamp}"
            this.address.text = "Lat: ${lat},Long: ${long}"

        }
    }
}