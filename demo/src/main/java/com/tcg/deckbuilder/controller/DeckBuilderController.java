package com.tcg.deckbuilder.controller;

import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.model.Deck;
import com.tcg.deckbuilder.service.CardService;
import com.tcg.deckbuilder.service.DeckService;
import com.tcg.deckbuilder.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DeckBuilderController {

    private final CardService cardService;
    private final DeckService deckService;
    private final DataImportService dataImportService;

    @Autowired
    public DeckBuilderController(CardService cardService, DeckService deckService, DataImportService dataImportService) {
        this.cardService = cardService;
        this.deckService = deckService;
        this.dataImportService = dataImportService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<String> cardTypes = cardService.getAllCardTypes();
        
        if (cardTypes.isEmpty()) {
            model.addAttribute("noCards", true);
            return "deck-builder";
        }
        
        model.addAttribute("cardTypes", cardTypes);
        model.addAttribute("energyTypes", cardService.getAllEnergyTypes());
        model.addAttribute("rarities", cardService.getAllRarities());
        model.addAttribute("cardLevels", cardService.getAllCardLevels());
        model.addAttribute("flipCardOptions", cardService.getFlipCardOptions());
        model.addAttribute("cardsByType", cardService.getCardsByType());
        model.addAttribute("deck", deckService.getDeck());
        model.addAttribute("selectedType", cardTypes.get(0));
        return "deck-builder";
    }

    @PostMapping("/deck/add/{cardId}")
    public String addCardToDeck(@PathVariable Long cardId) {
        Optional<Card> cardOpt = cardService.getCardById(cardId);
        cardOpt.ifPresent(deckService::addCardToDeck);
        return "redirect:/";
    }

    @PostMapping("/deck/remove/{index}")
    public String removeCardFromDeck(@PathVariable int index) {
        deckService.removeCardFromDeck(index);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchCards(@RequestParam String query, Model model) {
        List<Card> cards = cardService.searchCards(query);
        model.addAttribute("cards", cards);
        model.addAttribute("searchQuery", query);
        model.addAttribute("energyTypes", cardService.getAllEnergyTypes());
        model.addAttribute("cardTypes", cardService.getAllCardTypes());
        model.addAttribute("flipCardOptions", cardService.getFlipCardOptions());
        return "search-results";
    }
    
    @GetMapping("/filter")
    public String filterCards(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String rarity,
            @RequestParam(required = false) String energyType,
            @RequestParam(required = false) String cardLevel,
            @RequestParam(required = false) String flipCard,
            Model model) {
        
        List<Card> cards = cardService.advancedSearch(title, cardType, rarity, energyType, cardLevel, flipCard);
        model.addAttribute("cards", cards);
        model.addAttribute("title", title);
        model.addAttribute("cardType", cardType);
        model.addAttribute("rarity", rarity);
        model.addAttribute("energyType", energyType);
        model.addAttribute("cardLevel", cardLevel);
        model.addAttribute("flipCard", flipCard);
        model.addAttribute("energyTypes", cardService.getAllEnergyTypes());
        model.addAttribute("cardTypes", cardService.getAllCardTypes());
        model.addAttribute("rarities", cardService.getAllRarities());
        model.addAttribute("cardLevels", cardService.getAllCardLevels());
        model.addAttribute("flipCardOptions", cardService.getFlipCardOptions());
        return "filter-results";
    }
    
    // Admin section for card management
    
    @GetMapping("/admin/cards")
    public String adminCards(Model model) {
        model.addAttribute("cards", cardService.getAllCards());
        return "admin/cards";
    }
    
    @GetMapping("/admin/cards/new")
    public String newCardForm(Model model) {
        model.addAttribute("card", new Card());
        model.addAttribute("cardTypes", cardService.getAllCardTypes());
        model.addAttribute("energyTypes", cardService.getAllEnergyTypes());
        model.addAttribute("rarities", cardService.getAllRarities());
        model.addAttribute("cardLevels", cardService.getAllCardLevels());
        return "admin/card-form";
    }
    
    @GetMapping("/admin/cards/edit/{id}")
    public String editCardForm(@PathVariable Long id, Model model) {
        Optional<Card> cardOpt = cardService.getCardById(id);
        if (cardOpt.isPresent()) {
            model.addAttribute("card", cardOpt.get());
            model.addAttribute("cardTypes", cardService.getAllCardTypes());
            model.addAttribute("energyTypes", cardService.getAllEnergyTypes());
            model.addAttribute("rarities", cardService.getAllRarities());
            model.addAttribute("cardLevels", cardService.getAllCardLevels());
            return "admin/card-form";
        }
        return "redirect:/admin/cards";
    }
    
    @PostMapping("/admin/cards/save")
    public String saveCard(@ModelAttribute Card card) {
        cardService.saveCard(card);
        return "redirect:/admin/cards";
    }
    
    @PostMapping("/admin/cards/delete/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return "redirect:/admin/cards";
    }

    @PostMapping("/admin/import")
    public String importCards(@RequestParam(required = false) String jsonData) {
        try {
            if (jsonData != null && !jsonData.trim().isEmpty()) {
                dataImportService.importCardsFromJsonString(jsonData);
            } else {
                dataImportService.importCardsFromJson("cards.json");
            }
            return "redirect:/admin/cards?success=import";
        } catch (IOException e) {
            return "redirect:/admin/cards?error=import";
        }
    }
}

