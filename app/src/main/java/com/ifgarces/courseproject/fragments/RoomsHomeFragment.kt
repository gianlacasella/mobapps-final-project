package com.ifgarces.courseproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.ifgarces.courseproject.R


/**
 * Home page for the online poker rooms feature.
 */
class RoomsHomeFragment : Fragment() {

    companion object {
        /**
         * This encapsulates the call of this fragment, so we do not deal with transactions and it's
         * more simple.
         * @param caller From the caller's activity.
         * @param widget_id Placeholder for this fragment's view.
         */
        public fun summon(caller :FragmentActivity, widget_id :Int) {
            val transactioner :FragmentTransaction = caller.supportFragmentManager.beginTransaction()
                .replace(widget_id, newInstance())
            transactioner.commit()
        }
        private fun newInstance() = RoomsHomeFragment()
    }

    private class _WidgetHandler(owner :View) {
        val onlineRoomsRecycler :RecyclerView = owner.findViewById(R.id.RoomsHome_recycler)
        val createButton        :Button = owner.findViewById(R.id.RoomsHome_CreateButton)
        val joinButton          :Button = owner.findViewById(R.id.RoomsHome_JoinButton)
    }; private lateinit var ui : _WidgetHandler

//    override fun onCreate(savedInstanceState :Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?) : View? {
        val view :View? = inflater.inflate(R.layout.fragment_rooms_home, container, false)
        this.ui = _WidgetHandler(owner=view!!)
        // this.ui.onlineRoomsRecycler.adapter = ...
        this.ui.createButton.setOnClickListener {

        }
        this.ui.joinButton.setOnClickListener {

        }
        return view
    }
}