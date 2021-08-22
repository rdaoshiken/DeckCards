package com.example.deckcard.model

sealed class CardSuit {
    object Spades : CardSuit()
    object Hearts : CardSuit()
    object Clubs: CardSuit()
    object Diamonds : CardSuit()
}
