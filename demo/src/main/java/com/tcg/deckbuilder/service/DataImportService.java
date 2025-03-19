package com.tcg.deckbuilder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.tcg.deckbuilder.model.Card;
import com.tcg.deckbuilder.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DataImportService {
    private static final Logger logger = LoggerFactory.getLogger(DataImportService.class);
    private final CardRepository cardRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataImportService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper to be more lenient
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Transactional
    public void importCardsFromJson(String jsonFilePath) throws IOException {
        logger.info("Starting import from file: {}", jsonFilePath);
        
        // Clear existing data
        cardRepository.deleteAll();
        logger.info("Cleared existing data");

        // Read JSON file from classpath
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonFilePath)) {
            if (inputStream == null) {
                throw new IOException("Could not find JSON file: " + jsonFilePath);
            }

            // Read the file content as string for debugging
            String jsonContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            logger.debug("JSON content: {}", jsonContent);

            // Parse JSON to List<Card>
            List<Card> cards = objectMapper.readValue(jsonContent, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Card.class));

            logger.info("Successfully parsed {} cards", cards.size());

            // Save all cards
            List<Card> savedCards = cardRepository.saveAll(cards);
            logger.info("Successfully saved {} cards to database", savedCards.size());
        } catch (Exception e) {
            logger.error("Error importing cards from JSON: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void importCardsFromJsonString(String jsonString) throws IOException {
        logger.info("Starting import from JSON string");
        
        // Clear existing data
        cardRepository.deleteAll();
        logger.info("Cleared existing data");

        try {
            // Parse JSON string to List<Card>
            List<Card> cards = objectMapper.readValue(jsonString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Card.class));

            logger.info("Successfully parsed {} cards", cards.size());

            // Save all cards
            List<Card> savedCards = cardRepository.saveAll(cards);
            logger.info("Successfully saved {} cards to database", savedCards.size());
        } catch (Exception e) {
            logger.error("Error importing cards from JSON string: {}", e.getMessage(), e);
            throw e;
        }
    }
} 