package com.tcg.deckbuilder.api;

import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.model.Deck;
import com.tcg.deckbuilder.service.CardService;
import com.tcg.deckbuilder.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/deck")
public class DeckRestController {

    private final DeckService deckService;
    private final CardService cardService;

    @Autowired
    public DeckRestController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<Deck> getCurrentDeck() {
        return ResponseEntity.ok(deckService.getDeck());
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getDeckCards() {
        return ResponseEntity.ok(deckService.getDeck().getCards());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Deck.CardCount>> getCardCounts() {
        return ResponseEntity.ok(deckService.getDeck().getCardCounts());
    }

    @PostMapping("/add/{cardId}")
    public ResponseEntity<Deck> addCardToDeck(@PathVariable Long cardId) {
        Optional<Card> cardOpt = cardService.getCardById(cardId);
        if (cardOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        deckService.addCardToDeck(cardOpt.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(deckService.getDeck());
    }

    @DeleteMapping("/remove/{index}")
    public ResponseEntity<Deck> removeCardFromDeck(@PathVariable int index) {
        if (index < 0 || index >= deckService.getDeck().getSize()) {
            return ResponseEntity.badRequest().build();
        }
        
        deckService.removeCardFromDeck(index);
        return ResponseEntity.ok(deckService.getDeck());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearDeck() {
        deckService.clearDeck();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDeckStats() {
        return ResponseEntity.ok(deckService.getDeckStatistics());
    }
}

