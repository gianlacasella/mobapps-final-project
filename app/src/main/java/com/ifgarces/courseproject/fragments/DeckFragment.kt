package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.adapters.CardsAdapter
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf


class DeckFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val deckRecycler :RecyclerView = owner.findViewById(R.id.deck_recyclerview)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) :View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_card_deck, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        (this.requireActivity() as PlanningActivity).let { planningActivity :PlanningActivity ->
            if (planningActivity.selectedJoinedPokerRoom == null) {
                planningActivity.infoDialog(
                    title = "No selected PokerRoom",
                    message = "Please select a PokerRoom you are member before viewing and voting cards on it",
                    onDismiss = {
                        //planningActivity.navigator.navigateUp()
                    },
                    icon = null
                )
                return null
            }

            UI.deckRecycler.layoutManager = GridLayoutManager(
                this.requireActivity().applicationContext,
                3,
                LinearLayoutManager.VERTICAL,
                false
            )
            UI.deckRecycler.setHasFixedSize(true)
            UI.deckRecycler.adapter = CardsAdapter(
                data = planningActivity.deckCardsViewModel.currentDeckCards
            )

            // Trick for updating the recycler after the API call for getting the decks finished, hopefully
            UI.deckRecycler.postDelayed({
                UI.deckRecycler.adapter?.notifyDataSetChanged()
                }, 1000
            )

            PokerApiHandler.getResultCall(
                onSuccess = { response :PokerRoomsApiClasses.GetResultResponse ->
                    // This try-catch is for detecting when the `DeckCardsViewModel` is not yet
                    // initialized. We do nothing in that case.
                    try {
                        // Setting PokerRoom deck fetched through API
                        val selectedDeck :Deck? = planningActivity.deckCardsViewModel.getDeckByName(name=response.deck.name)
                        if (selectedDeck == null) {
                            return@getResultCall
                        }
                        planningActivity.deckCardsViewModel.setUserDeck(selectedDeck)

                        // Try-catch for handling null response body
                        try {
                            // Reseting each card value to zero for setting score based on votation count,
                            // of the votes fetched online
                            if (response.result.count() > 0) {
                                planningActivity.deckCardsViewModel.currentDeckCards.forEach {
                                    it.value = 0.0
                                }
                                var userName :String
                                var votedCardLabel :String
                                response.result.forEach { resultItem :PokerRoomsApiClasses.GetResultResponseItem ->
                                    userName = resultItem.name
                                    votedCardLabel = resultItem.vote
                                    planningActivity.deckCardsViewModel.currentDeckCards.find { it.label == votedCardLabel }!!.value += 1
                                }
                            } else {
                                Logf("[DeckFragment] getResultCall had no results...")
                            }
                        }
                        catch (e :NullPointerException) {
                            Logf("[DeckFragment] getResultCall had no results...")
                        }
                    }
                    catch (e :UninitializedPropertyAccessException) {
                        Logf("[DeckFragment] DeckCardsViewModel not yet initialized.")
                    }
                },
                onFailure = { serverResponse :String? ->
                    planningActivity.toastf("Couldn't fetch other user's votes online, using local data")
                },
                token = ApiUser.getToken()!!,
                roomName = planningActivity.selectedJoinedPokerRoom!!.pokerRoomName
            )
        }

        return fragView
    }
}