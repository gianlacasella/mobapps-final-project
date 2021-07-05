package com.ifgarces.courseproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ifgarces.courseproject.models.Card

@Dao
interface CardsDAO {
    @Query(value = "DELETE FROM ${Card.TABLE_NAME}")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card :Card)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(card :Card)

    @Query(value = "SELECT * FROM ${Card.TABLE_NAME}")
    fun getAll() :List<Card>

    @Query(value = "SELECT * FROM ${Card.TABLE_NAME} WHERE id=:id")
    fun get(id :Int) :Card

    @Query(value = "DELETE FROM ${Card.TABLE_NAME} WHERE id=:id")
    fun delete(id :Int)

    @Query(value = "SELECT * FROM ${Card.TABLE_NAME} WHERE deck_id=:deck_id")
    fun getCardsFromDeck(deck_id :Int) :List<Card>
}
