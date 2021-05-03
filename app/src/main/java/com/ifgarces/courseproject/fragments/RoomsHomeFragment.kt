package com.ifgarces.courseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.R
import com.ifgarces.courseproject.utils.Logf


/**
 * Home page for the online poker rooms feature.
 */
class RoomsHomeFragment : Fragment() {

    private class FragmentUI(owner :View) {
        val onlineRoomsRecycler :RecyclerView = owner.findViewById(R.id.roomsHome_recycler)
        val createButton        :Button = owner.findViewById(R.id.roomsHome_CreateButton)
        val joinButton          :Button = owner.findViewById(R.id.roomsHome_JoinButton)
    }; private lateinit var UI :FragmentUI

    override fun onCreateView(
        inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?
    ) : View? {
        val fragView :View? = inflater.inflate(R.layout.fragment_rooms_home, container, false)
        this.UI = FragmentUI(owner=fragView!!)
        this.UI.onlineRoomsRecycler.layoutManager = LinearLayoutManager(this.requireContext())
        this.UI.onlineRoomsRecycler.adapter = (this.requireActivity() as PlanningActivity).roomsRecyclerAdapter
        this.UI.createButton.setOnClickListener {
            (this.requireActivity() as PlanningActivity).navigator.nativateToRoomCreateFragment()
        }
        this.UI.joinButton.setOnClickListener {
            /* nothing here by the moment */
        }
        Logf("[RoomsHomeFragment] I was initialized.")
        return fragView
    }
}