package com.ifgarces.courseproject

import androidx.navigation.findNavController

class Navigator(private val activity: PlanningActivity?){
    fun navigateUp() {
        activity?.supportFragmentManager?.popBackStack()
    }

    fun navigateToDeckFragment() {
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.deckFragment)
    }

    fun navigateToRoomsHomeFragment() {
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.roomsHomeFragment)
    }

    fun navigateToSettingsFragment() {
        activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.settingsFragment)
    }

    fun nativateToRoomCreateFragment() {
        this.activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.roomCreateFragment)
    }

    fun nativateToRoomDetailsFragment() {
        this.activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.roomDetailsFragment)
    }

    fun navigateToSingleCardFragment() {
        this.activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.singleCardFragment)
    }

    fun navigateToJoinRoomFragment(){
        this.activity?.findNavController(R.id.fragmentContainer)?.navigate(R.id.roomJoinFragment)
    }
}