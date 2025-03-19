package com.tcg.deckbuilder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        }
    }

    public int getSize() {
        return cards.size();
    }

    public Map<String, CardCount> getCardCounts() {
        Map<String, CardCount> cardCounts = new HashMap<>();
        
        for (Card card : cards) {
            String cardName = card.getName();
            if (!cardCounts.containsKey(cardName)) {
                cardCounts.put(cardName, new CardCount(card, 0));
            }
            CardCount count = cardCounts.get(cardName);
            count.setCount(count.getCount() + 1);
        }
        
        return cardCounts;
    }

    public static class CardCount {
        private Card card;
        private int count;

        public CardCount(Card card, int count) {
            this.card = card;
            this.count = count;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

