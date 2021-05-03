package com.ifgarces.courseproject.models

import com.ifgarces.courseproject.enums.DeckType
import com.ifgarces.courseproject.utils.Logf

/**
 * Holds data that otherwise would die when the owner fragment is dismissed.
 */
object DataMaster {
    // We implement a getter for `userCardDeck` but not for `pokerRoomList`, as we want that mutable
    // list to be modified by reference on the recycler of `RoomsHomeFragment`.

    public  lateinit var pokerRoomList :MutableList<PokerRoom>
    private lateinit var userCardDeck  :Deck; fun getUserCardDeck() = this.userCardDeck

    private val DEFAULT_DECK_TYPE :DeckType = DeckType.Standard

    /**
     * Fills itself with some test data. This is supposed to be called at the very app startup, at
     * `MainActivity`.
     */
    public fun init() {
        Logf("[DataMaster] Initialzing...")
        this.pokerRoomList = mutableListOf()
        this.setDeckType(this.DEFAULT_DECK_TYPE)
    }

    /**
     * Changes the user deck according to the given DeckType.
     */
    public fun setDeckType(deckType :DeckType) {
        Logf("[DataMaster] Setting user card deck type to %s", deckType.toString())
        this.userCardDeck = Deck(
            type = deckType,
            cards = when (deckType) {
                DeckType.Fibonacci->{
                    mutableListOf(
                        Card("0", 0.0),
                        Card("1", 1.0),
                        Card("2", 2.0),
                        Card("3", 3.0),
                        Card("5", 5.0),
                        Card("8", 8.0),
                        Card("13", 13.0),
                        Card("21", 21.0),
                        Card("34", 34.0),
                        Card("55", 55.0),
                        Card("89", 89.0),
                        Card("144", 144.0),
                        Card("∞", Double.POSITIVE_INFINITY)
                    )
                }
                DeckType.Hours->{
                    mutableListOf(
                        Card("0", 0.0),
                        Card("1", 1.0),
                        Card("2", 2.0),
                        Card("3", 3.0),
                        Card("4", 4.0),
                        Card("6", 6.0),
                        Card("8", 8.0),
                        Card("16", 16.0),
                        Card("24", 24.0),
                        Card("32", 32.0),
                        Card("40", 40.0)
                    )
                }
                DeckType.T_Shirt->{
                    mutableListOf(
                        Card("XS", 1.0),
                        Card("S", 2.0),
                        Card("M", 3.0),
                        Card("L", 4.0),
                        Card("XL", 5.0),
                        Card("XXL", 6.0)
                    )
                }
                DeckType.Standard->{
                    mutableListOf(
                        Card("0", 0.0),
                        Card("1/2", 0.5),
                        Card("1", 1.0),
                        Card("2", 2.0),
                        Card("3", 3.0),
                        Card("5", 5.0),
                        Card("8", 8.0),
                        Card("13", 13.0),
                        Card("20", 20.0),
                        Card("40", 40.0),
                        Card("100", 100.0),
                        Card("∞", Double.POSITIVE_INFINITY)
                    )
                }
            }
        )
        this.userCardDeck.cards.add(
            Card("?", -1.0)
        )
        this.userCardDeck.cards.add(
            Card("☕", -1.0)
        )
    }
}