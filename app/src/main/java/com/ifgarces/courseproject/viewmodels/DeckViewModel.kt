package com.ifgarces.courseproject.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ifgarces.courseproject.Navigator
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.enums.DeckTypes
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.models.Deck

const val POSITIVE_INFINITY: Double = Double.POSITIVE_INFINITY
val DEFAULT_STARTING_DECK: DeckTypes = DeckTypes.Standard

class DeckViewModel(app: Application) : AndroidViewModel(app){
    var deck : Deck = Deck(DeckTypes.Standard, mutableListOf<Card>())

    init{
        deck = generateDeck(DEFAULT_STARTING_DECK)
    }

    private fun generateDeck(type: DeckTypes):Deck{
        val cards = mutableListOf<Card>()
        when (type){
            DeckTypes.Fibonacci->{
                cards.add(Card("0", 0.0))
                cards.add(Card("1", 1.0))
                cards.add(Card("2", 2.0))
                cards.add(Card("3", 3.0))
                cards.add(Card("5", 5.0))
                cards.add(Card("8", 8.0))
                cards.add(Card("13", 13.0))
                cards.add(Card("21", 21.0))
                cards.add(Card("34", 34.0))
                cards.add(Card("55", 55.0))
                cards.add(Card("89", 89.0))
                cards.add(Card("144", 144.0))
                cards.add(Card("∞", POSITIVE_INFINITY))
            }
            DeckTypes.Hours->{
                cards.add(Card("0", 0.0))
                cards.add(Card("1", 1.0))
                cards.add(Card("2", 2.0))
                cards.add(Card("3", 3.0))
                cards.add(Card("4", 4.0))
                cards.add(Card("6", 6.0))
                cards.add(Card("8", 8.0))
                cards.add(Card("16", 16.0))
                cards.add(Card("24", 24.0))
                cards.add(Card("32", 32.0))
                cards.add(Card("40", 40.0))
            }
            DeckTypes.T_Shirt->{
                cards.add(Card("XS", 1.0))
                cards.add(Card("S", 2.0))
                cards.add(Card("M", 3.0))
                cards.add(Card("L", 4.0))
                cards.add(Card("XL", 5.0))
                cards.add(Card("XXL", 6.0))
            }
            DeckTypes.Standard->{
                cards.add(Card("0", 0.0))
                cards.add(Card("1/2", 0.5))
                cards.add(Card("1", 1.0))
                cards.add(Card("2", 2.0))
                cards.add(Card("3", 3.0))
                cards.add(Card("5", 5.0))
                cards.add(Card("8", 8.0))
                cards.add(Card("13", 13.0))
                cards.add(Card("20", 20.0))
                cards.add(Card("40", 40.0))
                cards.add(Card("100", 100.0))
                cards.add(Card("∞", POSITIVE_INFINITY))
            }
        }
        return Deck(type, cards)
    }
}
