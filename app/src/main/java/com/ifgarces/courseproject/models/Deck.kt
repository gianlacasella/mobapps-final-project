package com.ifgarces.courseproject.models

import com.ifgarces.courseproject.enums.DeckType


data class Deck(
    public val type  :DeckType,
    public val cards :MutableList<Card>
) {
    public fun getCardsCount() :Int = cards.size
}