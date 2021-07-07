package com.ifgarces.courseproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.Logf
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * @property pokerRoomsNameToUsersMap Maps PokerRoom names (considered unique) and its corresponding
 * user list, queried asynchronously with `PokerApiHandler.getRoomCall`.
 */
class PokerRoomsViewModel(val planningActivity :PlanningActivity) : ViewModel() {

    public  val pokerRoomsList           :MutableList<PokerRoom> = mutableListOf()
    public  val pokerRoomsListLD         :MutableLiveData<MutableList<PokerRoom>> = MutableLiveData<MutableList<PokerRoom>>()
    private val pokerRoomsNameToUsersMap :MutableMap<String, List<PokerRoomsApiClasses.RoomMember>?> = mutableMapOf()

    public fun getUsersOfPokerRoom(roomName :String) = this.pokerRoomsNameToUsersMap[roomName]

    private val asyncExecutor :Executor = Executors.newSingleThreadExecutor()

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
//                        pokerRoomBuff = PokerRoom(name = item.roomName, password = null, onlineId = item.roomId) //! Cannot get password unless it is locally available, therefore the getAllRoomsCall is pointless.
//                    }
//                    throw NotImplementedError() //!
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

            this.mapRoomsWithUserList()

            onFinish.invoke()
        }
    }

    /**
     * Fills `pokerRoomsNameToUsersMap`, using getRoom API call for mapping every PokerRoom with the
     * list of its online users.
     */
    private fun mapRoomsWithUserList() {
        Logf("[PokerRoomsViewModel] Fetching user list for each PokerRoom (count=%d)", this.pokerRoomsList.count())
        this.pokerRoomsList.forEach { pokerRoom :PokerRoom ->
            PokerApiHandler.getRoomCall(
                onSuccess = { response :PokerRoomsApiClasses.GetRoomResponse ->
                    this.pokerRoomsNameToUsersMap[pokerRoom.name] = response.members
                },
                onFailure = { serverMessage :String? ->
                    Logf("[PokerRoomsViewModel] Couldn't perform getRoomCall for PokerRoom \"%s\"", pokerRoom.name)
                    this.pokerRoomsNameToUsersMap[pokerRoom.name] = null
                },
                token = ApiUser.getToken()!!,
                roomName = pokerRoom.name
            )
        }
    }

    /**
     * Adds a new PokerRoom (local model).
     */
    public fun appendPokerRoom(name :String, password :String, onlineId :String?) {
        val newPokerRoom :PokerRoom = PokerRoom(name, password, onlineId)
        this.pokerRoomsList.add(newPokerRoom)
        this.pokerRoomsListLD.value = this.pokerRoomsList

        this.asyncExecutor.execute {
            this.planningActivity.getRoomDB().pokerRoomsDAO().insert(room=newPokerRoom)
        }

        // Appending mapping to `pokerRoomsNameToUsersMap`
        Logf("[PokerRoomsViewModel] Fetching user list of appended PokerRoom \"%s\"", name)
        PokerApiHandler.getRoomCall(
            onSuccess = { response :PokerRoomsApiClasses.GetRoomResponse ->
                this.pokerRoomsNameToUsersMap[name] = response.members
            },
            onFailure = { serverMessage :String? ->
                Logf("[PokerRoomsViewModel] Couldn't perform getRoomCall for PokerRoom \"%s\"", name)
                this.pokerRoomsNameToUsersMap[name] = null
            },
            token = ApiUser.getToken()!!,
            roomName = name
        )
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
        this.pokerRoomsNameToUsersMap.remove(name)
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
