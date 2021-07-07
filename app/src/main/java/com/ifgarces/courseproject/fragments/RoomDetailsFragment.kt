package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.adapters.PokerUsersAdapter
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf
import com.ifgarces.courseproject.utils.yesNoDialog


/**
 * Shows the details of a single PokerRoom.
 */
class RoomDetailsFragment : Fragment() {
    
    private class FragmentUI(owner :View) {
        val name         :TextView = owner.findViewById(R.id.roomDetails_name)
        val usersCount   :TextView = owner.findViewById(R.id.roomDetails_onlineUsersCount)
        val onlineStatus :TextView = owner.findViewById(R.id.roomDetails_status)
        val usersRecycler :RecyclerView = owner.findViewById(R.id.roomDetails_usersRecycler)
        val selectButton :Button = owner.findViewById(R.id.roomDetails_selectButton)
        val deleteButton :Button = owner.findViewById(R.id.roomDetails_deleteButton)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) : View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_room_details, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        (this.requireActivity() as PlanningActivity).let { planningActivity :PlanningActivity ->
            val roomName :String = this.requireActivity().intent.getStringExtra(IntentKeys.POKER_ROOM_NAME)!!
            val roomRecyclerIndex :Int = this.requireActivity().intent.getIntExtra(IntentKeys.POKER_ROOM_RECYCLER_INDEX, -1)

            // Calculating the number of members, checking if we are joined to this specific poker room
//            val roomUsersCount :Int = if (planningActivity.selectedJoinedPokerRoom != null && planningActivity.selectedJoinedPokerRoom!!.onlineUsers != null) {
//                planningActivity.selectedJoinedPokerRoom!!.onlineUsers!!.count()
//            } else {
//                0
//            }

            UI.usersRecycler.layoutManager = LinearLayoutManager(planningActivity)
            try {
                UI.usersRecycler.adapter = PokerUsersAdapter(
                    data = planningActivity.pokerRoomsViewModel.getUsersOfPokerRoom(roomName)!!.toMutableList()
                )
            }
            catch (e :NullPointerException) {
                Logf("[RoomDetailsFragment] Couldn't set user list of offline PokerRoom.")
            }

            val pokerRoomUsers :List<PokerRoomsApiClasses.RoomMember>? = planningActivity.pokerRoomsViewModel.getUsersOfPokerRoom(roomName)
            val roomUsersCount :Int = if (pokerRoomUsers != null) pokerRoomUsers.count() else 0

            UI.name.text = roomName
            UI.usersCount.text = "Number of users: %d".format(roomUsersCount)

            UI.onlineStatus.text =
            if (planningActivity.pokerRoomsViewModel.getOnlineIdByRoomName(name=roomName) != null) {
                "PokerRoom status: ONLINE"
            } else {
                "PokerRoom Status: OFFLINE"
            }

            // If the PokerRoom being displayed matches the selected one, we disable the select button
            if (planningActivity.selectedJoinedPokerRoom?.pokerRoomName == roomName) {
                UI.selectButton.text = "This room is already selected"
                UI.selectButton.isEnabled = false
                UI.selectButton.isClickable = false
                UI.selectButton.background = ContextCompat.getDrawable(planningActivity, R.drawable.secondary_button_grayed_out)
            }

            UI.selectButton.setOnClickListener {
                planningActivity.selectedJoinedPokerRoom = PlanningActivity.SelectedJoinedPokerRoom(
                    pokerRoomName = roomName,
                    onlineUsers = null
                )
                planningActivity.toastf("Changed to: %s", roomName)
                //planningActivity.InitializeGeoLocator()
                planningActivity.navigator.navigateUp()
            }

            // We first try to delete the room online. If the API call succeededs, then we delete it
            // from the local database.
            UI.deleteButton.setOnClickListener {
                this.requireContext().yesNoDialog(
                    title = "Delete poker room",
                    message = "Are you sure?",
                    onYesClicked = {
                        val roomToDelete :PokerRoom = planningActivity.roomsRecyclerAdapter.getItemAt(roomRecyclerIndex)
                        if (! roomToDelete.isOnline()) {
                            Logf("[RoomDetailsFragment] Uh oh, the room wanted to be deleted has null `onlineId`. It is caused because of internet problems which avoided the room to be created online. Then, the delete request cannot be performed.")
                            planningActivity.infoDialog(
                                title = "Couldn't delete online poker room",
                                message = "The online metadata for the Poker Room is uninitialized, please check your internet connection, wait some seconds, and try again.",
                                icon = R.drawable.warning_icon
                            )
                        } else {
                            Logf("[RoomDetailsFragment] Deleting PokerRoom from API")
                            PokerApiHandler.deleteRoomCall(
                                onSuccess = {
                                    Logf("[RoomDetailsFragment] API delete call succeeded, now deleting PokerRoom from local database")
                                    planningActivity.toastf("PokerRoom \"%s\" deleted online", roomName)
                                    planningActivity.pokerRoomsViewModel.deletePokerRoomByName(name=roomName)
                                },
                                onFailure = { serverMessage :String? ->
                                    planningActivity.infoDialog(
                                        title = "Couldn't delete online poker room",
                                        message = serverMessage ?: "Please check your internet connection",
                                        icon = R.drawable.warning_icon
                                    )
                                },
                                token = ApiUser.getToken()!!,
                                roomName = roomName,
                                roomId = roomToDelete.onlineId!! // at this point, it cannot be null
                            )
                            planningActivity.navigator.navigateUp()
                        }
                    },
                    icon = R.drawable.warning_icon
                )
            }

            // Getting room details to show them
//            PokerApiHandler.getRoomCall(
//                onSuccess = {
//                    Logf("[RoomDetailsFragment] API get room call succeeded")
//                    planningActivity.toastf("PokerRoom \"%s\" details fetched", roomName)
//
//
//                    // Now, on 'it' object we have the API Response
//                    val APIResponseMembers = it.members
//                    var roomUsernamesAndDates = mutableListOf<PokerRoomUser>()
//                    APIResponseMembers.forEach{roomUsernamesAndDates.add(PokerRoomUser(
//                        it.username,
//                        it.location.lat,
//                        it.location.long,
//                        it.location.timestamp
//                    ))
//                    }
//                },
//                onFailure = { serverMessage :String? ->
//                    planningActivity.infoDialog(
//                        title = "Couldn't fetch poker room details",
//                        message = serverMessage ?: "Please check your internet connection",
//                        icon = R.drawable.warning_icon
//                    )
//                },
//                token = ApiUser.getToken()!!,
//                roomName = roomName
//            )
        }
        return fragView
    }

    data class PokerRoomUser(
        val username: String,
        val lat: String,
        val long: String,
        val timestamp: String
        )
}