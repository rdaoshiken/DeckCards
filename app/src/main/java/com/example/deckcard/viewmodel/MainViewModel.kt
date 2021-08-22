package com.example.deckcard.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.deckcard.model.CardSuit
import com.example.deckcard.model.DeckCard

class MainViewModel {

    private val MAX_VALUE = 13
    private val MIN_VALUE = 1

    private val PAIR = 1
    private val THREE = 2
    private val TWO_PAIR = 3
    private val FLUSH = 4
    private val STRAIGHT = 5
    private val FULLHOUSE = 6
    private val STRAIGHT_FLUSH = 7
    private val ROYAL_STRAIGHT_FLUSH = 8


    val cardList = MutableLiveData<ArrayList<DeckCard>>()
    val compareResult = MutableLiveData<Boolean>()

    fun shuffle() {
        var rtn = generateBaseCardList()

        for (i in 0..51) {
            val j = (0..51).random()
            if (i != j) {
                swap(rtn, i, j)
            }
        }

        cardList.postValue(rtn)
    }

    fun compareCards(fstCards: ArrayList<DeckCard>, sndCards: ArrayList<DeckCard>) {
        var result = false
        when (fstCards.size) {
            1 -> result = compareCard(fstCards[0], sndCards[0])
            else -> cardsCompare(fstCards, sndCards)
        }
        compareResult.postValue(result)
    }

    private fun generateBaseCardList(): ArrayList<DeckCard> {
        val rtn = ArrayList<DeckCard>()
        for (i in MIN_VALUE..MAX_VALUE) {
            rtn.add(DeckCard(CardSuit.Spades, i))
            rtn.add(DeckCard(CardSuit.Diamonds, i))
            rtn.add(DeckCard(CardSuit.Hearts, i))
            rtn.add(DeckCard(CardSuit.Spades, i))
        }
        return rtn
    }

    private fun getCardsValue(cards: ArrayList<DeckCard>): Int {
        var rtn = 0
        when {
            isRoyalStraight(cards) -> {
                rtn = ROYAL_STRAIGHT_FLUSH
            }
            isFlushStraight(cards) -> {
                rtn = STRAIGHT_FLUSH
            }
            isFullHouse(cards) -> {
                rtn = FULLHOUSE
            }
            isStraight(cards) -> {
                rtn = STRAIGHT
            }
            isFlush(cards) -> {
                rtn = FLUSH
            }
            isTwoPair(cards) -> {
                rtn = TWO_PAIR
            }
            isThree(cards) -> {
                rtn = THREE
            }
            isPair(cards) -> {
                rtn = PAIR
            }
        }

        return rtn
    }

    private fun sortCard(cards: ArrayList<DeckCard>): ArrayList<DeckCard> {
        var rtn = cards
        for (i in 1 until rtn.size) {
            var j = i
            while (j > 0 && rtn[j].number < rtn[j - 1].number) {
                swap(rtn, j - 1, j)
                j--
            }
        }
        return rtn
    }

    private fun swap(cards: ArrayList<DeckCard>, start: Int, end: Int) {
        val temp = cards[start]
        cards[start] = cards[end]
        cards[end] = temp
    }

    private fun findTheBig(cards: ArrayList<DeckCard>): DeckCard {
        var rtn = cards[0]
        for (i in 1 until cards.size) {
            if (cards[i].number > rtn.number || (cards[i].number == rtn.number && suitCompare(
                    cards[i],
                    rtn
                ))
            ) {
                rtn = cards[i]
            }
        }
        return rtn
    }


    // compare

    private fun compareCard(fstCard: DeckCard, sndCard: DeckCard): Boolean {
        if (fstCard.number == sndCard.number) {
            return if (fstCard.suit == CardSuit.Spades || sndCard.suit == CardSuit.Clubs) {
                true
            } else if (fstCard.suit == CardSuit.Clubs || sndCard.suit == CardSuit.Spades) {
                false
            } else {
                fstCard.suit == CardSuit.Hearts
            }
        }
        return fstCard.number > sndCard.number
    }

    private fun suitCompare(fstCard: DeckCard, sndCard: DeckCard): Boolean {
        return when (fstCard.suit) {
            CardSuit.Spades -> true
            CardSuit.Hearts -> sndCard.suit != CardSuit.Spades
            CardSuit.Diamonds -> sndCard.suit == CardSuit.Clubs
            else -> false
        }
    }

    private fun straightFlushCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        return if (fstCards[0].suit == sndCards[0].suit) {
            straightCompare(fstCards, sndCards)
        } else {
            suitCompare(fstCards[0], sndCards[0])
        }
    }

    private fun fullHouseCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {

        return false
    }

    private fun flushCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        //TODO:need to check flush rule
        return false
    }

    private fun straightCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        return if (fstCards[0].number == sndCards[0].number) {
            suitCompare(fstCards[0], sndCards[0])
        } else {
            fstCards[0].number > sndCards[0].number
        }
    }

    private fun twoPairCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        //TODO:need to check Two pair rule
        return false
    }

    private fun threeCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        return compareCard(fstCards[0], sndCards[0])
    }

    private fun pairCompare(fstCards: ArrayList<DeckCard>, sndCards: ArrayList<DeckCard>): Boolean {
        return if (isPair(fstCards) && isPair(sndCards)) {
            compareCard(fstCards[0], sndCards[0])
        } else if (!isPair(fstCards) && !isPair(sndCards)) {
            sortCard(fstCards)[1].number > sortCard(sndCards)[1].number
        } else {
            isPair(fstCards)
        }
    }

    private fun cardsCompare(
        fstCards: ArrayList<DeckCard>,
        sndCards: ArrayList<DeckCard>
    ): Boolean {
        val fstCardValue = getCardsValue(fstCards)
        val sndCardValue = getCardsValue(sndCards)
        if (fstCardValue == sndCardValue) {
            when (fstCardValue) {
                ROYAL_STRAIGHT_FLUSH -> {
                    return flushCompare(fstCards, sndCards)
                }
                STRAIGHT_FLUSH -> {
                    return straightFlushCompare(fstCards, sndCards)
                }
                FULLHOUSE -> {
                    return fullHouseCompare(fstCards, sndCards)
                }
                STRAIGHT -> {
                    return straightCompare(fstCards, sndCards)
                }
                FLUSH -> {
                    return flushCompare(fstCards, sndCards)
                }
                TWO_PAIR -> {
                    return twoPairCompare(fstCards, sndCards)
                }
                THREE -> {
                    return threeCompare(fstCards, sndCards)
                }
                PAIR -> {
                    return pairCompare(fstCards, sndCards)
                }
                else -> {
                    var fstCard = findTheBig(fstCards)
                    var sndCard = findTheBig(sndCards)
                    return if (fstCard.number == sndCard.number) {
                        suitCompare(fstCard, sndCard)
                    } else
                        fstCard.number > sndCard.number
                }
            }
        } else {
            return fstCardValue > sndCardValue
        }
    }


    //type check

    private fun isRoyalStraight(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size == 5) {
            val tmp = sortCard(cards)
            if (tmp[0].number == 1 && tmp[1].number == 10) {
                for (i in 2..4) {
                    if (tmp[i].number - tmp[i - 1].number != 1) {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    private fun isFlushStraight(cards: ArrayList<DeckCard>) = (isFlush(cards) && isStraight(cards))

    private fun isFullHouse(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size == 5) {
            val tmp = sortCard(cards)
            val twoTmp = ArrayList<DeckCard>()
            val threeTmp = ArrayList<DeckCard>()
            var isLS = false
            isLS = when {
                tmp[2] == tmp[1] -> {
                    true
                }
                tmp[2] == tmp[3] -> {
                    false
                }
                else -> {
                    return false
                }
            }

            for (i in 0..4) {
                if (isLS) {
                    if (i < 3) {
                        threeTmp.add(tmp[i])
                    } else {
                        twoTmp.add(tmp[i])
                    }
                } else {
                    if (i < 2) {
                        twoTmp.add(tmp[i])
                    } else {
                        threeTmp.add(tmp[i])
                    }
                }
            }
            return isPair(twoTmp) && isThree(threeTmp)
        }
        return false
    }

    private fun isFlush(cards: ArrayList<DeckCard>) =
        (cards.size == 5 && (cards[0].suit == cards[1].suit && cards[2].suit == cards[3].suit
                && cards[3].suit == cards[4].suit && cards[1].suit == cards[3].suit))

    private fun isStraight(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size == 5) {
            val tmp = sortCard(cards)
            var startIndex = 1
            if (tmp[0].number == 1 && tmp[1].number == 10) {
                startIndex = 2
            }
            for (i in startIndex..4) {
                if (tmp[i].number - tmp[i - 1].number != 1) {
                    return false
                }
            }
            return true
        }
        return false
    }

    private fun isTwoPair(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size >= 4) {
            var tmp = sortCard(cards)
            if (tmp[0].number == tmp[1].number) {
                return (tmp[3].number == tmp[2].number || tmp[3].number == tmp[4].number)
            } else if (tmp[1].number == tmp[2].number) {
                return tmp[3].number == tmp[4].number
            }
        }
        return false
    }

    private fun isThree(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size >= 3) {
            for (i in 0 until cards.size - 2) {
                var tmp = cards[i]
                for (j in 1 until cards.size) {
                    var result = 0
                    if (tmp.number == cards[j].number) {
                        result += 1
                    }
                    if (result == 2) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isPair(cards: ArrayList<DeckCard>): Boolean {
        if (cards.size >= 2) {
            for (i in 0 until cards.size) {
                val tmp = cards[i]
                for (j in i + 1 until cards.size) {
                    if (tmp.number == cards[j].number) {
                        return true
                    }
                }
            }
        }
        return false
    }
}