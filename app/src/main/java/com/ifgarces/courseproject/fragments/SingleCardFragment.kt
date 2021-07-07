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
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.IntentKeys
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.infoDialog
import com.ifgarces.courseproject.utils.toastf
import kotlin.math.roundToInt


class SingleCardFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val cardLabel     :TextView = owner.findViewById(R.id.singleCard_label)
        val cardVoteScore :TextView = owner.findViewById(R.id.singleCard_value)
        val voteButton    :Button = owner.findViewById(R.id.singleCard_voteButton)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) :View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_single_card, container, false)
        this.UI = FragmentUI(owner = fragView!!)

        (this.requireActivity() as PlanningActivity).let { planningActivity: PlanningActivity ->
            val cardLabel :String = this.requireActivity().intent.getStringExtra(IntentKeys.CARD_LABEL)!!
            val cardScore :Double = this.requireActivity().intent.getDoubleExtra(IntentKeys.CARD_VALUE, -1.0)

            Logf("[SingleCardFragment] Viewing card with label %s and votation score %s", cardLabel, cardScore.toString())

            UI.cardLabel.text = cardLabel
            UI.cardVoteScore.text = "Votes: %d".format(cardScore.roundToInt())

            UI.cardLabel.setOnClickListener {
                planningActivity.navigator.navigateUp()
            }

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

            UI.voteButton.setOnClickListener {
                PokerApiHandler.voteCall(
                    onSuccess = { response: PokerRoomsApiClasses.SimpleResponse ->
                        planningActivity.toastf("Success on voting online for card")
                        planningActivity.deckCardsViewModel.getCardByLabel(cardLabel)!!.value += 1
                        UI.cardVoteScore.text = "Votes: %d".format(planningActivity.deckCardsViewModel.getCardByLabel(cardLabel)!!.value.roundToInt())
                    },
                    onFailure = { serverMessage :String? ->
                        planningActivity.toastf("Couldn't send vote online, try again")
                    },
                    token = ApiUser.getToken()!!,
                    roomName = planningActivity.selectedJoinedPokerRoom!!.pokerRoomName,
                    vote = cardLabel
                )
            }
            return fragView
        }
    }

}