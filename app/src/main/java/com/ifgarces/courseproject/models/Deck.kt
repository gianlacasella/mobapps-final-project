package com.ifgarces.courseproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a card deck. A Deck has many Cards.
 * @property id The unique identifier for the Deck.
 * @property name The name (formerly `deckType`), which indicates the cards belonging to this Deck.
 */
@Entity(tableName=Deck.TABLE_NAME)
data class Deck(
    @PrimaryKey(autoGenerate=false)
    val id :Int,
    val name :String
) {
    companion object {
        const val TABLE_NAME :String = "deck"
    }
}
