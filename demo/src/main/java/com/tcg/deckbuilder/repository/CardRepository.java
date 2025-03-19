package com.tcg.deckbuilder.repository;

import com.tcg.deckbuilder.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    // Find cards by card type
    List<Card> findByCardType(String cardType);
    
    // Find cards by card type title
    List<Card> findByCardTypeTitle(String cardTypeTitle);
    
    // Find cards by grade (rarity)
    List<Card> findByGrade(String grade);
    
    // Find cards by energy type
    List<Card> findByEnergyType(String energyType);
    
    // Find cards by energy type title
    List<Card> findByEnergyTypeTitle(String energyTypeTitle);
    
    // Find cards by product category
    List<Card> findByProductCategory(String productCategory);
    
    // Find cards by card level
    List<Card> findByCardLevel(String cardLevel);
    
    // Find cards by title containing the search term (case insensitive)
    List<Card> findByTitleContainingIgnoreCase(String searchTerm);
    
    // Find cards by card number
    List<Card> findByCardNo(String cardNo);
    
    // Find cards by artist
    List<Card> findByArtistTitleContainingIgnoreCase(String artistName);
    
    // Find FLIP cards
    @Query("SELECT c FROM Card c WHERE " +
           "c.cardDesc LIKE '%<strong>FLIP</strong>%' OR " +
           "c.cardDesc LIKE '%<STRONG>FLIP</STRONG>%' OR " +
           "c.cardDesc LIKE '%&lt;strong&gt;FLIP&lt;/strong&gt;%'")
    List<Card> findFlipCards();
    
    // Count cards by card type
    @Query("SELECT COUNT(c) FROM Card c WHERE c.cardType = :cardType")
    Long countByCardType(@Param("cardType") String cardType);
    
    // Count cards by grade (rarity)
    @Query("SELECT COUNT(c) FROM Card c WHERE c.grade = :grade")
    Long countByGrade(@Param("grade") String grade);
    
    // Count cards by energy type
    @Query("SELECT COUNT(c) FROM Card c WHERE c.energyType = :energyType")
    Long countByEnergyType(@Param("energyType") String energyType);
    
    // Advanced search with multiple criteria
    @Query("SELECT c FROM Card c WHERE " +
           "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:cardType IS NULL OR c.cardType = :cardType) AND " +
           "(:rarity IS NULL OR c.grade = :rarity) AND " +
           "(:energyType IS NULL OR " +
           "  (:energyType = 'Mix' AND c.energyTypeTitle LIKE '%Mix%') OR " +
           "  (:energyType != 'Mix' AND c.energyTypeTitle = :energyType)) AND " +
           "(:cardLevel IS NULL OR c.cardLevel = :cardLevel) AND " +
           "(:flipCard IS NULL OR " +
           "  (:flipCard = 'Yes' AND (c.cardDesc LIKE '%<strong>FLIP</strong>%' OR " +
           "                        c.cardDesc LIKE '%<STRONG>FLIP</STRONG>%' OR " +
           "                        c.cardDesc LIKE '%&lt;strong&gt;FLIP&lt;/strong&gt;%')) OR " +
           "  (:flipCard = 'No' AND NOT (c.cardDesc LIKE '%<strong>FLIP</strong>%' OR " +
           "                            c.cardDesc LIKE '%<STRONG>FLIP</STRONG>%' OR " +
           "                            c.cardDesc LIKE '%&lt;strong&gt;FLIP&lt;/strong&gt;%')))")
    List<Card> advancedSearch(
        @Param("title") String title,
        @Param("cardType") String cardType,
        @Param("rarity") String rarity,
        @Param("energyType") String energyType,
        @Param("cardLevel") String cardLevel,
        @Param("flipCard") String flipCard
    );
    
    // Get all distinct card types
    @Query("SELECT DISTINCT c.cardTypeTitle FROM Card c ORDER BY c.cardTypeTitle")
    List<String> findAllCardTypes();
    
    // Get all distinct energy types
    @Query("SELECT DISTINCT c.energyTypeTitle FROM Card c ORDER BY c.energyTypeTitle")
    List<String> findAllEnergyTypes();
    
    // Get all distinct grades (rarities)
    @Query("SELECT DISTINCT c.grade FROM Card c ORDER BY c.grade")
    List<String> findAllGrades();
    
    // Get all distinct card levels
    @Query("SELECT DISTINCT c.cardLevelTitle FROM Card c ORDER BY c.cardLevelTitle")
    List<String> findAllCardLevels();
}

