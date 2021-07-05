package com.ifgarces.courseproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ifgarces.courseproject.PlanningActivity
import com.ifgarces.courseproject.models.Card
import com.ifgarces.courseproject.models.Deck
import com.ifgarces.courseproject.networking.ApiUser
import com.ifgarces.courseproject.networking.PokerApiHandler
import com.ifgarces.courseproject.networking.PokerRoomsApiClasses
import com.ifgarces.courseproject.utils.Logf
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * ViewModel for Card and Deck models.
 * @property currentDeck Current Deck of the user.
 * @property currentDeckCards Cards belonging to the current Deck.
 * @property allDecks All the possible Decks.
 * @property presetDecks Default collection of Decks when the API call for getting the Decks fails.
 * Then, the default Deck assigned to `currentDeck` will be the first of these items, unless another
 * one was stored in the local database.
 */
class DeckCardsViewModel : ViewModel() {

    public lateinit var currentDeck :Deck
    public lateinit var currentDeckLiveData :MutableLiveData<Deck>

    @Volatile public lateinit var currentDeckCards :MutableList<Card>
    public lateinit var currentDeckCardsLiveData :MutableLiveData<MutableList<Card>>

    private lateinit var asyncExecutor :Executor

    public lateinit var allDecks    :List<Deck>
    public lateinit var allCards    :List<Card>
    private val         presetDecks :List<PokerRoomsApiClasses.Deck> = listOf( // in offline mode, the default user deck will be the first one of these presets
        PokerRoomsApiClasses.Deck(
            name = "Standard",
            cards = listOf("0", "1/2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "∞", "?", "☕")
        ),
        PokerRoomsApiClasses.Deck(
            name = "Fibonacci", cards = listOf("0", "1", "2", "3", "5", "8", "13", "21", "34", "55", "89", "144", "∞", "?", "☕")
        ),
        PokerRoomsApiClasses.Deck(
            name = "Hours", cards = listOf("0", "1", "2", "3", "4", "6", "8", "16", "24", "32", "40", "?", "☕")
        ),
        PokerRoomsApiClasses.Deck(
            name = "T-Shirt", cards = listOf("XS", "S", "M", "L", "XL", "XXL", "?", "☕")
        )
    )

    public fun init(activity :PlanningActivity) {
        this.currentDeckLiveData = MutableLiveData<Deck>()
        this.currentDeckCards = mutableListOf()
        this.currentDeckCardsLiveData = MutableLiveData<MutableList<Card>>()
        this.asyncExecutor = Executors.newSingleThreadExecutor()

        this.asyncExecutor.execute {
            // Getting all decks (and their linked cards)
            this.getDecksAsync(
                onApiCallFinished = { decks :List<Deck>, cards :List<Card> ->
                    this.allDecks = decks
                    //TODO: store/load current user selected Deck (offline, from local database)

                    this.currentDeck = decks.first()
                    this.allCards = cards
                    this.currentDeckCards = this.getCardsOfDeck(this.currentDeck.id).toMutableList()
                    Logf("[DeckCardsViewModel] Initialized with %d decks and %d cards. Current deck is %s, with %d cards",
                        this.allDecks.count(), this.allCards.count(), this.currentDeck.name, this.currentDeckCards.count()
                    )
                },
                planningActivity = activity
            )
        }
    }

    /**
     * Updates the current single Deck instance.
     */
    public fun setUserDeck(deck :Deck) {
        this.currentDeck = deck
        this.currentDeckLiveData.value = this.currentDeck

        this.currentDeckCards = this.getCardsOfDeck(deck.id).toMutableList()
        // Saving to local database
        asyncExecutor.execute {
            //TODO: save to proper single current user Deck DAO.
        }
    }

    /**
     * **This function must be executed in a separate thread.**
     * Tries to get the possible decks from the API, otherwise loads local presets. Assigns the got
     * result to the `deck` attribute of this class.
     * @param onApiCallFinished Callback executed when the API call finishes (any status).
     */
    private fun getDecksAsync(
        onApiCallFinished :(decks :List<Deck>, cardsOfDeck :List<Card>) -> Unit,
        planningActivity :PlanningActivity
    ) {
        /**
         * Converts the list of `PokerRoomsApiClasses.Deck` into a list of `Deck` from the models,
         * saves the result in the local database and returns it.
         */
        fun apiDecksToModelDecks(apiItems :List<PokerRoomsApiClasses.Deck>) :Pair<List<Deck>, List<Card>> {
            val modelItems :MutableList<Deck> = mutableListOf()
            val cardsOfDeck :MutableList<Card> = mutableListOf()
            Logf("[DeckCardsViewModel] Clearing decks and cards from database before inserting new ones...")
            planningActivity.getRoomDB().decksDAO().clear()
            planningActivity.getRoomDB().cardsDAO().clear()
            Logf("[DeckCardsViewModel] Inserting %d decks in database, and their cards", apiItems.count())
            apiItems.forEachIndexed { deckIndex :Int, deck :PokerRoomsApiClasses.Deck ->
                modelItems.add(
                    Deck(id=deckIndex, name=deck.name)
                )
                planningActivity.getRoomDB().decksDAO().insert(modelItems.last())
                deck.cards.forEachIndexed { cardIndex :Int, cardName :String ->
                    val newCard :Card = Card(id=cardIndex, label=cardName, value=0.0, deck_id=deckIndex)
                    cardsOfDeck.add(newCard)
                    planningActivity.getRoomDB().cardsDAO().insert(newCard)
                }
            }
            return Pair(modelItems.toList(), cardsOfDeck)
        }

        PokerApiHandler.getAllDecksCall(
            onSuccess = {
                this.asyncExecutor.execute {
                    val decksCards = apiDecksToModelDecks(it)
                    onApiCallFinished.invoke(decksCards.first, decksCards.second)
                }
            },
            onFailure = { serverMessage :String? ->
                Logf("[DeckCardsViewModel] Couldn't fetch decks from API, using local presets instead")
                this.asyncExecutor.execute {
                    val decksCards = apiDecksToModelDecks(presetDecks)
                    onApiCallFinished.invoke(decksCards.first, decksCards.second)
                }
            },
            token = ApiUser.getToken()!!
        )
    }

    /**
     * Gets the collection of cards for the `Deck` whose `id` matches the given `deckId`
     */
    public fun getCardsOfDeck(deckId :Int) :List<Card> {
        return this.allCards.filter { it.deck_id == deckId }
    }
}
