package com.ifgarces.courseproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.utils.Logf
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class PokerRoomsViewModel(private val planningActivity :PlanningActivity) : ViewModel() {

    public  val pokerRoomsList   :MutableList<PokerRoom> = mutableListOf()
    public  val pokerRoomsListLD :MutableLiveData<MutableList<PokerRoom>> = MutableLiveData<MutableList<PokerRoom>>()
    private val asyncExecutor    :Executor = Executors.newSingleThreadExecutor()

    /**
     * Loads the PokerRooms from the local database. It would be impossible to use
     * `PokerApiHandler.getAllRoomsCall` to do this, as we need the password for each one, which is
     * only known at creation time and therefore it is only available at the local database. There
     * is not an API call for getting the password of any PokerRoom.
     */
    public fun init(onFinish :() -> Unit) {
        this.asyncExecutor.execute {
//            PokerApiHandler.getAllRoomsCall(
//                onSuccess = {
//                    // API call succeeded, loading got PokerRooms in the recycler
//                    var pokerRoomBuff :PokerRoom
//                    it.rooms.forEachIndexed { index :Int, item :PokerRoomsApiClasses.GetAllRoomsResponseItem ->
//                        pokerRoomBuff = PokerRoom(name = item.roomName, password = null, onlineId = item.roomId)
//                    }
//                    throw NotImplementedError()
//                    onFinish.invoke()
//                },
//                onFailure = { serverMessage :String? ->
//                    // API call failed, loading PokerRooms from local database
//                    Logf("[PokerRoomsViewModel] Couldn't get all rooms from API, loading from local database instead")
//                    this.planningActivity.getRoomDB().pokerRoomsDAO().getAll().forEach {
//                        this.pokerRoomsList.add(it)
//                    }
//                    this.pokerRoomsListLD.value = this.pokerRoomsList
//                    onFinish.invoke()
//                },
//                token = ApiUser.getToken()!!
//            )
            Logf("[PokerRoomsViewModel] Loading PokerRooms from local database")
            this.planningActivity.getRoomDB().pokerRoomsDAO().getAll().forEach {
                this.pokerRoomsList.add(it)
            }
            onFinish.invoke()
        }
    }

    public fun appendPokerRoom(name :String, password :String, onlineId :String?) {
        val newPokerRoom :PokerRoom = PokerRoom(name, password, onlineId)
        this.pokerRoomsList.add(newPokerRoom)
        this.pokerRoomsListLD.value = this.pokerRoomsList

        this.asyncExecutor.execute {
            this.planningActivity.getRoomDB().pokerRoomsDAO().insert(room=newPokerRoom)
        }
    }

    /**
     * Finds the PokerRoom by name and deletes it, as that attribute is the primary key.
     */
    public fun deletePokerRoomByName(name :String) {
        val toDeleteRoom :PokerRoom? = this.pokerRoomsList.find { it.name == name }
        if (toDeleteRoom == null) {
            Logf("[PokerRoomsViewModel] Fatal: could not delete, as none PokerRoom with name \"%s was found\"", name)
            return
        }
        this.pokerRoomsList.remove(toDeleteRoom)
        this.pokerRoomsListLD.value = this.pokerRoomsList
        this.asyncExecutor.execute {
            this.planningActivity.getRoomDB().pokerRoomsDAO().delete(name)
        }
    }

    /**
     * Finds the `PokerRoom` whose name is `roomName` and updates its `onlineId` attribute.
     */
    public fun updateOnlineIdOfRoom(roomName :String, onlineId :String) {
        val targetPokerRoom :PokerRoom = this.pokerRoomsList.find { it.name == roomName }!!
        Logf("[PokerRoomsViewModel] Updating %s: setting onlineID to %s", targetPokerRoom, targetPokerRoom.onlineId)
        targetPokerRoom.onlineId = onlineId
    }

    /**
     * Searches for the `PokerRoom` whose name is `name` (locally), and returns its `onlineID`
     */
    public fun getOnlineIdByRoomName(name :String) :String? {
        return this.pokerRoomsList.find { it.name == name }?.onlineId
    }
}
