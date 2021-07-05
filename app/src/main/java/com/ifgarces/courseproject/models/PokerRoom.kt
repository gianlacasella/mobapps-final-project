package com.ifgarces.courseproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represent an online poker play room.
 * @property name Name that identifies the poker room (must be unique).
 * @property password Password needed at the moment an user wants to enter to this room and play.
 * @property onlineId ID given by the API when creating it online. Will be null if we couldn't
 * perform the request and we temportarily stored the `PokerRoom` in the database. Remember to fill
 * it when that API call can be made. Note that this is `var` and not `val`, as it is intended to be
 * updated when null.
 */
@Entity(tableName=PokerRoom.TABLE_NAME)
data class PokerRoom(
    @PrimaryKey(autoGenerate=false)
    val name :String,
    val password :String,
    var onlineId :String?
) {
    companion object {
        const val TABLE_NAME :String = "PokerRoom"
    }
}
