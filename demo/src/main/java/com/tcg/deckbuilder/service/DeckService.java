package com.tcg.deckbuilder.service;

import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.model.Deck;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SessionScope
public class DeckService {

    private final Deck deck = new Deck();

    public Deck getDeck() {
        return deck;
    }

    public void addCardToDeck(Card card) {
        deck.addCard(card);
    }

    public void removeCardFromDeck(int index) {
        deck.removeCard(index);
    }

    public int getDeckSize() {
        return deck.getSize();
    }
    
    public void clearDeck() {
        deck.getCards().clear();
    }
    
    public Map<String, Object> getDeckStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic stats
        stats.put("totalCards", deck.getSize());
        
        // Cards by type
        Map<String, Long> typeStats = deck.getCards().stream()
                .collect(Collectors.groupingBy(Card::getType, Collectors.counting()));
        stats.put("typeDistribution", typeStats);
        
        // Cards by rarity
        Map<String, Long> rarityStats = deck.getCards().stream()
                .collect(Collectors.groupingBy(Card::getRarity, Collectors.counting()));
        stats.put("rarityDistribution", rarityStats);
        
        return stats;
    }
}

