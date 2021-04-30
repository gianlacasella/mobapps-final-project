package com.ifgarces.courseproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlanningActivity: AppCompatActivity() {
    var navigator = Navigator(this)

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_planning)

        val deck_button = findViewById<View>(R.id.deck_item) as View
        deck_button.setOnClickListener{
            navigator.navigateToDeckFragment()
        }

        val rooms_button = findViewById<View>(R.id.rooms_item) as View
        rooms_button.setOnClickListener{
            navigator.navigateToRoomsFragment()
        }

        val settings_button = findViewById<View>(R.id.settings_item) as View
        settings_button.setOnClickListener{
            navigator.navigateToSettingsFragment()
        }

        /*
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.deck_item -> {
                    true
                }
                R.id.rooms_item -> {
                    true
                }
                R.id.settings_item -> {
                    true
                }
                else -> false
            }
        }*/
    }
}