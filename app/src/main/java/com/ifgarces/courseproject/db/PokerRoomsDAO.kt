package com.ifgarces.courseproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ifgarces.courseproject.models.PokerRoom


@Dao
interface PokerRoomsDAO {
    @Query(value = "DELETE FROM ${PokerRoom.TABLE_NAME}")
    fun clear()

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insert(room :PokerRoom)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(room :PokerRoom)

    @Query(value = "SELECT * FROM ${PokerRoom.TABLE_NAME}")
    fun getAll() :List<PokerRoom>

    @Query(value = "SELECT * FROM ${PokerRoom.TABLE_NAME} WHERE name=:name")
    fun get(name :String) :PokerRoom

    @Query(value = "DELETE FROM ${PokerRoom.TABLE_NAME} WHERE name=:name")
    fun delete(name :String)
}
