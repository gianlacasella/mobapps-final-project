package com.ifgarces.courseproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ifgarces.courseproject.models.Deck


@Dao
interface DecksDAO {
    @Query(value = "DELETE FROM ${Deck.TABLE_NAME}")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deck :Deck)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(deck :Deck)

    @Query(value = "SELECT * FROM ${Deck.TABLE_NAME}")
    fun getAll() :List<Deck>

    @Query(value = "SELECT * FROM ${Deck.TABLE_NAME} WHERE id=:id")
    fun get(id :Int) :Deck

    @Query(value = "DELETE FROM ${Deck.TABLE_NAME} WHERE id=:id")
    fun delete(id :Int)
}
