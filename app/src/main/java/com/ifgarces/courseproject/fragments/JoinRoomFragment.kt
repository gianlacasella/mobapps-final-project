package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf


class JoinRoomFragment : Fragment() {

    private class FragmentUI(owner: View) {
        val roomNameTextBox     :EditText = owner.findViewById(R.id.roomsJoin_name)
        val roomPasswordTextBox :EditText = owner.findViewById(R.id.roomsJoin_password)
        val confirmButton       :Button = owner.findViewById(R.id.roomsJoin_commitButton)
    }; private lateinit var UI: FragmentUI

    public object CurrentJoinedPokerRoom {
        // Ugly solution for getting current joined room members without affecting `PokerRoom` model
        var pokerRoomName :String? = null
        var onlineUsers :List<String>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_join_room, container, false)
        this.UI = FragmentUI(owner = fragView!!)

        this.UI.confirmButton.setOnClickListener {
            if (this.UI.roomNameTextBox.text.isBlank() || this.UI.roomPasswordTextBox.text.isBlank()) {
                this.requireContext().toastf("Please fill all the fields")
                return@setOnClickListener
            }

            val roomName :String = UI.roomNameTextBox.text.toString()
            val roomPassword :String = UI.roomPasswordTextBox.text.toString()

            // Executing API call
            (this.requireActivity() as PlanningActivity).let { activity :PlanningActivity ->
                val roomOnlineID :String? = activity.pokerRoomsViewModel.getOnlineIdByRoomName(roomName)
                Logf("[JoinRoomFragment] Joining room with name %s, password %s and onlineID %s...", roomName, roomPassword, roomOnlineID)
                PokerApiHandler.joinRoomCall(
                    onSuccess = {
                        try {
                            Logf(
                                "[JoinRoomFragment] User joined to room with %d participants! API response message: %s",
                                it.members.count(), it.message
                            )
                            activity.toastf("Successfuly joined to online poker room")
                            activity.pokerRoomsViewModel.appendPokerRoom(roomName, roomPassword, roomOnlineID)
                            CurrentJoinedPokerRoom.onlineUsers = it.members
                            CurrentJoinedPokerRoom.pokerRoomName = roomName
                            activity.navigator.navigateUp() //this.onBackPressed()
                        }
                        catch (e :NullPointerException) {
                            Logf("[JoinRoomFragment] Join request was OK, but response shape was unexpected") // <- this should not happen if the API was done right, as it is an error response case. Happens when we try to join a room from another user (unexpected token, apparently)
                            activity.infoDialog(
                                title = "Couldn't join to Poker Room",
                                message = it.message,
                                icon = R.drawable.warning_icon
                            )
                        }
                    },
                    onFailure = { serverMessage :String? ->
                        activity.infoDialog(
                            title = "Couldn't join to online poker room",
                            message = serverMessage ?: "Please check your internet connection",
                            icon = R.drawable.warning_icon
                        )
                    },
                    roomName = roomName,
                    roomPassword = roomPassword,
                    token = ApiUser.getToken()!!
                )
            }
        }

        return fragView
    }
}