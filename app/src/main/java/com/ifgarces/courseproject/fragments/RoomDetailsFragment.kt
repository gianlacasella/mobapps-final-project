package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.yesNoDialog


/**
 * Shows the details of a single PokerRoom.
 */
class RoomDetailsFragment : Fragment() {
    
    private class FragmentUI(owner :View) {
        val name         :TextView = owner.findViewById(R.id.roomDetails_name)
        val usersCount   :TextView = owner.findViewById(R.id.roomDetails_onlineUsersCount)
        val deleteButton :Button = owner.findViewById(R.id.roomDetails_deleteButton)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) : View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_room_details, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        val roomName :String = this.requireActivity().intent.getStringExtra(IntentKeys.POKER_ROOM_NAME)!!
        val roomRecyclerIndex :Int = this.requireActivity().intent.getIntExtra(IntentKeys.POKER_ROOM_RECYCLER_INDEX, -1)
        val roomUsersCount :Int = 0 // this would be obtained through an API query, etc.

        this.UI.name.text = roomName
        this.UI.usersCount.text = "Number of users: ${roomUsersCount}"

        this.UI.deleteButton.setOnClickListener {
            Logf("[RoomDetailsFragment] Deleting room with name %s and index %d", roomName, roomRecyclerIndex)

            this.requireContext().yesNoDialog(
                title = "Delete poker room",
                message = "Are you sure?",
                onYesClicked = {
                    (this.requireActivity() as PlanningActivity).roomsRecyclerAdapter.removeAt(
                        index=roomRecyclerIndex
                    )
                    this.requireActivity().onBackPressed()
                },
                onNoClicked = {},
                icon = R.drawable.warning_icon
            )
        }

        return fragView
    }
}