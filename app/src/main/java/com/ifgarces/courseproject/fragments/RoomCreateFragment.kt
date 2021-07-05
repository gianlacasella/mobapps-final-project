package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf
import java.util.concurrent.Executors


class RoomCreateFragment : Fragment() {

    private class FragmentUI(owner: View) {
        val roomName        : EditText = owner.findViewById(R.id.roomsCreate_name)
        val roomPassword    : EditText = owner.findViewById(R.id.roomsCreate_password)
        val confirmButton   : Button = owner.findViewById(R.id.roomsCreate_commitButton)
        val deckTypeSpinner : Spinner = owner.findViewById(R.id.createroom_deckTypeSpinner)
    }; private lateinit var UI: FragmentUI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val fragView: View? = inflater.inflate(R.layout.fragment_room_create, container, false)
        this.UI = FragmentUI(owner = fragView!!)

        val deckNames :List<String> = (this.requireActivity() as PlanningActivity).deckCardsViewModel.allDecks.map { it.name }
        UI.deckTypeSpinner.adapter = ArrayAdapter(
            this.requireContext(), R.layout.spinner_item, deckNames
        )

        UI.deckTypeSpinner.setSelection(
            deckNames.indexOf(
                (this.requireActivity() as PlanningActivity).deckCardsViewModel.currentDeck.name
            )
        )

        UI.confirmButton.setOnClickListener {
            if (UI.roomName.text.isBlank() || UI.roomPassword.text.isBlank()) {
                this.requireContext().toastf("Please fill all the fields")
                return@setOnClickListener
            }

            val roomName :String = UI.roomName.text.toString()
            val roomPassword :String = UI.roomPassword.text.toString()
            Logf("[RoomCreateFragment] Adding room with name %s and password %s to RoomsHomeFragment's recycler...", roomName, roomPassword)
            val selectedDeck :PokerRoomsApiClasses.Deck = getApiDeck(UI.deckTypeSpinner.selectedItem.toString())!!

            Executors.newSingleThreadExecutor().execute {
                (this.requireActivity() as PlanningActivity).let { activity :PlanningActivity ->
                    // Calling the API to create the room remotely
                    PokerApiHandler.createRoomCall(
                        onSuccess = {
                            activity.toastf("PokerRoom \"%s\" created online", roomName)
                            activity.pokerRoomsViewModel.appendPokerRoom(name=roomName, password=roomPassword, onlineId=it.roomId)
                        },
                        onFailure = { serverMessage :String? ->
                            activity.infoDialog(
                                title = "Couldn't create online poker room",
                                message = (serverMessage ?: "Please check your internet connection") +
                                        "\nYour room will be created locally and will be published online when internet is available again",
                                onDismiss = {
                                    activity.onCreateRoomApiFail(roomName, roomPassword, selectedDeck)
                                },
                                icon = R.drawable.warning_icon
                            )
                            activity.pokerRoomsViewModel.appendPokerRoom(name=roomName, password=roomPassword, onlineId=null)
                        },
                        roomName = roomName,
                        roomPassword = roomPassword,
                        roomDeck = selectedDeck,
                        token = ApiUser.getToken()!!
                    )

                    activity.deckCardsViewModel.currentDeck = activity.getRoomDB().decksDAO().getAll().find { it.name == selectedDeck.name }!!
                }
                this.requireActivity().onBackPressed()
            }
        }
        return fragView
    }

    /**
     * Returns the `PokerRoomsApiClasses.Deck` representing the stored model `Deck` whose name
     * matches the given `deckName`.
     */
    private fun getApiDeck(deckName :String) :PokerRoomsApiClasses.Deck? {
        (this.requireActivity() as PlanningActivity).let { activity :PlanningActivity ->
            activity.deckCardsViewModel.allDecks.forEach { deck :Deck ->
                if (deck.name == deckName) {
                    val modelCards :List<Card> = activity.deckCardsViewModel.getCardsOfDeck(deckId = deck.id)
                    val apiCards :List<String> = modelCards.map { it.label }

                    return PokerRoomsApiClasses.Deck(
                        name = deck.name,
                        cards = apiCards
                    )
                }
            }
        }
        return null
    }
}