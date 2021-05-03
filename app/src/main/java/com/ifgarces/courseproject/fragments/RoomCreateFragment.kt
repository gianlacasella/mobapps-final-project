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
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.toastf


class RoomCreateFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val roomName      :EditText = owner.findViewById(R.id.roomsCreate_name)
        val roomPassword  :EditText = owner.findViewById(R.id.roomsCreate_password)
        val confirmButton :Button = owner.findViewById(R.id.roomsCreate_commitButton)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ): View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_room_create, container, false)
        this.UI = FragmentUI(owner=fragView!!)

        this.UI.confirmButton.setOnClickListener {

            if (this.UI.roomName.text.isBlank() || this.UI.roomPassword.text.isBlank()) {
                this.requireContext().toastf("Please fill all the fields")
                return@setOnClickListener
            }

            val newRoom :PokerRoom = PokerRoom(
                name = this.UI.roomName.text.toString(),
                password = this.UI.roomPassword.text.toString()
            )
            Logf("[RoomCreateFragment] Adding item %s to RoomsHomeFragment's recycler...", newRoom)
            (this.requireActivity() as PlanningActivity).roomsRecyclerAdapter.addItem(newRoom)

            this.requireActivity().onBackPressed()
        }

        return fragView
    }
}