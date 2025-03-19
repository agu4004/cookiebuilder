package com.tcg.deckbuilder.api;

import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardRestController {

    private final CardService cardService;

    public CardRestController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        Optional<Card> card = cardService.getCardById(id);
        return card.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Card>> getCardsByType(@PathVariable String type) {
        List<Card> cards = cardService.getCardsByCardType(type);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/energy/{energyType}")
    public ResponseEntity<List<Card>> getCardsByEnergyType(@PathVariable String energyType) {
        List<Card> cards = cardService.getCardsByEnergyType(energyType);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<List<Card>> getCardsByRarity(@PathVariable String rarity) {
        List<Card> cards = cardService.getCardsByRarity(rarity);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Card>> searchCards(@RequestParam String query) {
        List<Card> cards = cardService.searchCards(query);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Card>> filterCards(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String rarity,
            @RequestParam(required = false) String energyType,
            @RequestParam(required = false) String cardLevel) {
        
        List<Card> filteredCards = cardService.advancedSearch(title, cardType, rarity, energyType, cardLevel);
        return ResponseEntity.ok(filteredCards);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllCardTypes() {
        return ResponseEntity.ok(cardService.getAllCardTypes());
    }

    @GetMapping("/energyTypes")
    public ResponseEntity<List<String>> getAllEnergyTypes() {
        return ResponseEntity.ok(cardService.getAllEnergyTypes());
    }

    @GetMapping("/rarities")
    public ResponseEntity<List<String>> getAllRarities() {
        return ResponseEntity.ok(cardService.getAllRarities());
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card savedCard = cardService.saveCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card card) {
        Optional<Card> existingCard = cardService.getCardById(id);
        if (existingCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        card.setId(id);
        Card updatedCard = cardService.saveCard(card);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        Optional<Card> existingCard = cardService.getCardById(id);
        if (existingCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCardStats() {
        return ResponseEntity.ok(cardService.getCardStatistics());
    }
}

