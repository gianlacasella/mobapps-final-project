package com.ifgarces.courseproject

import androidx.navigation.findNavController

class Navigator(val activity: PlanningActivity?){
    fun navigateUp(){
        activity?.supportFragmentManager?.popBackStack()
    }

    fun navigateToDeckFragment(){
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.deckFragment)
    }

    fun navigateToRoomsFragment(){
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.roomsHomeFragment)
    }

    fun navigateToSettingsFragment(){
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.settingsFragment)
    }
}