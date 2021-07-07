package com.ifgarces.courseproject.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/**
 * Represents a card of the poker game of this project. A Card belongs to one and only one Deck.
 * @property id Primary key for Room DB.
 * @property label The display name (e.g. "K", "?", "☕", etc.).
 * @property value Score of the card in the game, i.e. the number of user votes in an online
 * PokerRoom game.
 * @property deck_id Foreign key reference to the Deck this Card belongs to.
 */
@Entity(
    tableName = Card.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = Deck::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("deck_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Card(
    @PrimaryKey(autoGenerate=false)
    val id :Int,
    val label :String,
    var value :Double, //TODO: change to Int

    @ColumnInfo(index = true)
    val deck_id :Int
) {
    companion object {
        const val TABLE_NAME :String = "card"
    }
}
