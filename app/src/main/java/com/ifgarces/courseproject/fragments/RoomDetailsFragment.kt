package com.ifgarces.courseproject.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
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
        val roomUsersCount :Int

        // Calculating the number of members, checking if we are joined to this specific poker room
        if (JoinRoomFragment.CurrentJoinedPokerRoom.onlineUsers != null && JoinRoomFragment.CurrentJoinedPokerRoom.pokerRoomName!! == roomName) {
            roomUsersCount = JoinRoomFragment.CurrentJoinedPokerRoom.onlineUsers!!.count()
        } else {
            roomUsersCount = 0
        }

        this.UI.name.text = roomName
        this.UI.usersCount.text = "Number of users: ${roomUsersCount}"

        // We first try to delete the room online. If the API call succeeded, then we delete it from
        // the local database.
        this.UI.deleteButton.setOnClickListener {
            this.requireContext().yesNoDialog(
                title = "Delete poker room",
                message = "Are you sure?",
                onYesClicked = {
                    (this.requireActivity() as PlanningActivity).let { activity :PlanningActivity ->
                        val roomToDelete :PokerRoom = activity.roomsRecyclerAdapter.getItemAt(roomRecyclerIndex)
                        if (roomToDelete.onlineId == null) {
                            Logf("[RoomDetailsFragment] Uh oh, the room wanted to be deleted has null `onlineId`. It is caused because of internet problems which avoided the room to be created online. Then, the delete request cannot be performed.")
                            activity.infoDialog(
                                title = "Couldn't delete online poker room",
                                message = "The online metadata for the Poker Room is uninitialized, please check your internet connection, wait some seconds, and try again.",
                                icon = R.drawable.warning_icon
                            )
                        } else {
                            Logf("[RoomDetailsFragment] Deleting PokerRoom from API")
                            PokerApiHandler.deleteRoomCall(
                                onSuccess = {
                                    Logf("[RoomDetailsFragment] API delete call succeeded, now deleting PokerRoom from local database")
                                    activity.pokerRoomsViewModel.deletePokerRoomByName(name = roomName)
                                    activity.roomsRecyclerAdapter.removeAt(roomRecyclerIndex)
                                },
                                onFailure = { serverMessage :String? ->
                                    activity.infoDialog(
                                        title = "Couldn't delete online poker room",
                                        message = serverMessage ?: "Please check your internet connection",
                                        icon = R.drawable.warning_icon
                                    )
                                },
                                token = ApiUser.getToken()!!,
                                roomName = roomName,
                                roomId = roomToDelete.onlineId!! // at this point, it cannot be null
                            )
                            activity.navigator.navigateUp() //this.onBackPressed()
                        }
                    }
                },
                icon = R.drawable.warning_icon
            )
        }

        return fragView
    }
}