package com.ifgarces.courseproject

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ifgarces.courseproject.adapters.PokerRoomsAdapter
import com.ifgarces.courseproject.db.LocalRoomDB
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.Logf
import com.ifgarces.courseproject.utils.toastf
import com.ifgarces.courseproject.viewmodel.DeckCardsViewModel
import com.ifgarces.courseproject.viewmodel.PokerRoomsViewModel
import java.util.concurrent.Executors


/**
 * The user is authed (logged in) when he is in `PlanningActivity`. So we consider he logs out when
 * this activity is closed (and when he gets back to `MainActivity`).
 * @property CREATE_ONLINE_RETRY_SECONDS States the number of seconds to wait after another try for
 * `PokerApiHandler.createRoomCall`, when creating a `PokerRoom` online fails.
 */
class PlanningActivity : AppCompatActivity() {

    private class ActivityUI(owner :AppCompatActivity) {
        val navigationMenu :BottomNavigationView = owner.findViewById(R.id.bottom_navigation)
        val topActionBar   :MaterialToolbar = owner.findViewById(R.id.topAppBar)
    }; private lateinit var UI :ActivityUI

    private val CREATE_ONLINE_RETRY_SECONDS :Long = 5

    // Declaring local Room database
    private lateinit var localDB :LocalRoomDB; public fun getRoomDB() = this.localDB

    // Exposing Navigator and ViewModels
    public lateinit var navigator           :Navigator
    public lateinit var pokerRoomsViewModel :PokerRoomsViewModel
    public lateinit var deckCardsViewModel  :DeckCardsViewModel

    // Exposing PokerRooms RecyclerView adapter
    public lateinit var roomsRecyclerAdapter :PokerRoomsAdapter
//    public lateinit var cardsRecyclerAdapter :CardsAdapter

    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_planning)
        this.UI = ActivityUI(owner=this)

        Logf("[PlanningActivity] Building Room DB...")
        this.localDB = Room.databaseBuilder(this, LocalRoomDB::class.java, "poker_db").build()
        Executors.newSingleThreadExecutor().execute {
            Logf(
                "[PlanningActivity] Local database loaded with %d cards, %d decks and %d poker rooms",
                this.localDB.cardsDAO().getAll().count(),
                this.localDB.decksDAO().getAll().count(),
                this.localDB.pokerRoomsDAO().getAll().count()
            )
        }

        this.navigator = Navigator(activity=this)
        this.pokerRoomsViewModel = PokerRoomsViewModel(planningActivity=this)
        this.pokerRoomsViewModel.init(
            onFinish = {
                this.roomsRecyclerAdapter = PokerRoomsAdapter(data=this.pokerRoomsViewModel.pokerRoomsList)
            }
        )
        // Assigning livedata values here to avoid 'java.lang.IllegalStateException: Cannot invoke setValue on a background thread'
        this.pokerRoomsViewModel.pokerRoomsListLD.value = this.pokerRoomsViewModel.pokerRoomsList

        this.deckCardsViewModel = DeckCardsViewModel()
        this.deckCardsViewModel.init(activity=this)
        this.deckCardsViewModel.currentDeckCardsLiveData.value = this.deckCardsViewModel.currentDeckCards

        //this.roomsRecyclerAdapter = PokerRoomsAdapter(data=this.pokerRoomsViewModel.pokerRoomsList)

        UI.navigationMenu.setOnNavigationItemSelectedListener { item :MenuItem ->
            when(item.itemId) {
                R.id.deck_item -> {
                    navigator.navigateToDeckFragment()
                    UI.navigationMenu.menu.findItem(R.id.rooms_item).title = ""
                    UI.navigationMenu.menu.findItem(R.id.settings_item).title = ""
                    item.title = "Deck"
                    true
                }
                R.id.rooms_item -> {
                    navigator.navigateToRoomsHomeFragment()
                    UI.navigationMenu.menu.findItem(R.id.deck_item).title = ""
                    UI.navigationMenu.menu.findItem(R.id.settings_item).title = ""
                    item.title = "Rooms"
                    true
                }
                R.id.settings_item->{
                    navigator.navigateToSettingsFragment()
                    UI.navigationMenu.menu.findItem(R.id.rooms_item).title = ""
                    UI.navigationMenu.menu.findItem(R.id.deck_item).title = ""
                    item.title = "Settings"
                    true
                }
                else -> false
            }
        }

        UI.topActionBar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.log_out_button -> {
                    this.finish() // getting back to MainActivity (authentication screen)
                    true
                }
                else -> false
            }
        }
    }

    /**
     * This recursive function is supossed to be called after a `PokerRoom` could not be created
     * online, in `RoomCreateFragment`. This method was moved to the activity, so it will still
     * execute after that fragment is dismissed. Stores the `PokerRoom` in the local database and
     * launches a routine that constantly tries to perform the create call for it, until the
     * response is OK (according to `CREATE_ONLINE_RETRY_SECONDS`).
     */
    public fun onCreateRoomApiFail(roomName :String, roomPassword :String, selectedDeck :PokerRoomsApiClasses.Deck) {
        Logf("[PlanningActivity] Retrying createRoom API call...")
        PokerApiHandler.createRoomCall(
            onSuccess = {
                // When the request finally succeeds, we assign the got onlineID to the
                // corresponding PokerRoom model instance.
                Logf("[PlanningActivity] createRoom API call finally succeeded")
                this.toastf("PokerRoom \"%s\" created online (background)", roomName)
                this.pokerRoomsViewModel.updateOnlineIdOfRoom(roomName=roomName, onlineId=it.roomId)
            },
            onFailure = { _ :String? ->
                // If we fail, we wait some time and try again, recursively (because this is
                // asynchronous)
                Thread.sleep(CREATE_ONLINE_RETRY_SECONDS * 1000)
                this.onCreateRoomApiFail(roomName, roomPassword, selectedDeck)
            },
            token = ApiUser.getToken()!!,
            roomName = roomName,
            roomPassword = roomPassword,
            roomDeck = selectedDeck
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        // The user leaves this activity if and only if he wants to log out.
        ApiUser.logOut()
        this.toastf("Logged out")
    }
}