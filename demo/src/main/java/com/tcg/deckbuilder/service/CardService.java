package com.tcg.deckbuilder.service;

import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional(readOnly = true)
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<String, List<Card>> getCardsByType() {
        // Group cards by card type title using Java streams
        return getAllCards().stream()
                .collect(Collectors.groupingBy(Card::getCardTypeTitle));
    }
    
    @Transactional(readOnly = true)
    public Map<String, List<Card>> getCardsByEnergyType() {
        // Group cards by energy type title using Java streams
        return getAllCards().stream()
                .collect(Collectors.groupingBy(Card::getEnergyTypeTitle));
    }

    @Transactional(readOnly = true)
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<String> getAllCardTypes() {
        return cardRepository.findAllCardTypes();
    }
    
    @Transactional(readOnly = true)
    public List<String> getAllEnergyTypes() {
        return cardRepository.findAllEnergyTypes();
    }
    
    @Transactional(readOnly = true)
    public List<String> getAllRarities() {
        List<String> allGrades = cardRepository.findAllGrades();
        // Define the desired order
        List<String> orderedRarities = List.of(
            "COMMON",
            "UNCOMMON",
            "RARE",
            "EXTRA RARE",
            "SUPER RARE",
            "SECRET RARE",
            "ULTRA RARE",
            "SECRET SUPER RARE",
            "SECRET ULTRA RARE",
            "PROMOTION"
        );
        
        // Filter and sort the grades according to the desired order
        return allGrades.stream()
            .filter(orderedRarities::contains)
            .sorted((a, b) -> orderedRarities.indexOf(a) - orderedRarities.indexOf(b))
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<String> getAllCardLevels() {
        return cardRepository.findAllCardLevels();
    }
    
    @Transactional(readOnly = true)
    public List<String> getFlipCardOptions() {
        return List.of("Yes", "No");
    }
    
    @Transactional(readOnly = true)
    public List<Card> searchCards(String searchTerm) {
        return cardRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
    
    @Transactional(readOnly = true)
    public List<Card> getCardsByCardType(String cardType) {
        return cardRepository.findByCardTypeTitle(cardType);
    }
    
    @Transactional(readOnly = true)
    public List<Card> getCardsByEnergyType(String energyType) {
        return cardRepository.findByEnergyTypeTitle(energyType);
    }
    
    @Transactional(readOnly = true)
    public List<Card> getCardsByRarity(String rarity) {
        return cardRepository.findByGrade(rarity);
    }
    
    @Transactional(readOnly = true)
    public List<Card> getCardsByCardLevel(String cardLevel) {
        return cardRepository.findByCardLevel(cardLevel);
    }
    
    @Transactional(readOnly = true)
    public List<Card> getFlipCards() {
        return cardRepository.findFlipCards();
    }
    
    @Transactional(readOnly = true)
    public List<Card> getCardsByArtist(String artistName) {
        return cardRepository.findByArtistTitleContainingIgnoreCase(artistName);
    }
    
    @Transactional(readOnly = true)
    public List<Card> advancedSearch(String title, String cardType, String rarity, 
                                    String energyType, String cardLevel, String flipCard) {
        return cardRepository.advancedSearch(title, cardType, rarity, energyType, cardLevel, flipCard);
    }
    
    // Overload for backward compatibility
    @Transactional(readOnly = true)
    public List<Card> advancedSearch(String title, String cardType, String rarity, 
                                    String energyType, String cardLevel) {
        return advancedSearch(title, cardType, rarity, energyType, cardLevel, null);
    }
    
    @Transactional
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
    
    @Transactional
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Map<String, Object> getCardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total cards
        stats.put("totalCards", cardRepository.count());
        
        // Cards by type
        Map<String, Long> typeStats = new HashMap<>();
        getAllCardTypes().forEach(type -> 
            typeStats.put(type, cardRepository.countByCardType(type))
        );
        stats.put("typeDistribution", typeStats);
        
        // Cards by energy type
        Map<String, Long> energyStats = new HashMap<>();
        getAllEnergyTypes().forEach(energy -> 
            energyStats.put(energy, cardRepository.countByEnergyType(energy))
        );
        stats.put("energyDistribution", energyStats);
        
        // Cards by grade (rarity)
        Map<String, Long> rarityStats = new HashMap<>();
        getAllRarities().forEach(rarity ->
            rarityStats.put(rarity, cardRepository.countByGrade(rarity))
        );
        stats.put("rarityDistribution", rarityStats);
        
        // FLIP card statistics
        long flipCardCount = getFlipCards().size();
        stats.put("flipCards", flipCardCount);
        stats.put("nonFlipCards", cardRepository.count() - flipCardCount);
        
        return stats;
    }
}

