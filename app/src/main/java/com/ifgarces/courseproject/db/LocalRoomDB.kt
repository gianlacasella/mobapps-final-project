package com.ifgarces.courseproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.models.PokerRoom
import com.ifgarces.courseproject.utils.Logf

/**
 * Deck <-- 1:N --> Card
 */
@Database(
    entities = [PokerRoom::class, Deck::class, Card::class],
    version = 1,
    exportSchema = false
)
abstract class LocalRoomDB : RoomDatabase() {
//    abstract fun currentDeckDAO()
    abstract fun decksDAO()      :DecksDAO
    abstract fun cardsDAO()      :CardsDAO
    abstract fun pokerRoomsDAO() :PokerRoomsDAO
}
