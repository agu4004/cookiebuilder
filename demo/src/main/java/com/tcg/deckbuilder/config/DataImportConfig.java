package com.tcg.deckbuilder.config;

import com.tcg.deckbuilder.service.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DataImportConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataImportConfig.class);

    @Autowired
    private DataImportService dataImportService;

    @Bean
    @Order(2) // Run after database initialization
    public CommandLineRunner importData() {
        return args -> {
            try {
                logger.info("Starting data import process...");
                // Import data from JSON file
                dataImportService.importCardsFromJson("cards.json");
                logger.info("Successfully imported cards from JSON");
            } catch (Exception e) {
                logger.error("Failed to import cards from JSON: {}", e.getMessage(), e);
                throw e;
            }
        };
    }
} 