package com.ifgarces.courseproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ifgarces.courseproject.adapters.PokerRoomsAdapter
import com.ifgarces.courseproject.models.DataMaster

class PlanningActivity: AppCompatActivity() {

    public val navigator = Navigator(this)
    public lateinit var roomsRecyclerAdapter :PokerRoomsAdapter

    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_planning)

        this.roomsRecyclerAdapter = PokerRoomsAdapter(data=DataMaster.pokerRoomList)

        val navigationMenu = findViewById(R.id.bottom_navigation) as BottomNavigationView
        navigationMenu.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.deck_item -> {
                    navigator.navigateToDeckFragment()
                    navigationMenu.menu.findItem(R.id.rooms_item).title = ""
                    navigationMenu.menu.findItem(R.id.settings_item).title = ""
                    item.title = "Deck"
                    true
                }
                R.id.rooms_item -> {
                    navigator.navigateToRoomsHomeFragment()
                    navigationMenu.menu.findItem(R.id.deck_item).title = ""
                    navigationMenu.menu.findItem(R.id.settings_item).title = ""
                    item.title = "Rooms"
                    true
                }
                R.id.settings_item->{
                    navigator.navigateToSettingsFragment()
                    navigationMenu.menu.findItem(R.id.rooms_item).title = ""
                    navigationMenu.menu.findItem(R.id.deck_item).title = ""
                    item.title = "Settings"
                    true
                }
                else -> false
            }
        }
    }
}