package com.ifgarces.courseproject.models


/**
 * Represents a card of this strange Poker version.
 * @property label The display name (e.g. "K", "?", "☕", etc.).
 * @property value Score of the card in the game.
 */
data class Card(
    val label :String,
    val value :Double
)
