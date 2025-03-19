-- Drop table if exists
DROP TABLE IF EXISTS cards;

-- Create cards table with Cookie Run card structure
CREATE TABLE cards (
    id BIGINT PRIMARY KEY,
    element_id BIGINT,
    title VARCHAR(255) NOT NULL,
    artist_title VARCHAR(255),
    product_title VARCHAR(255),
    card_desc VARCHAR(2000),
    rarity VARCHAR(50),
    hp VARCHAR(50),
    card_no VARCHAR(50),
    grade VARCHAR(50),
    card_image VARCHAR(500),
    product_category VARCHAR(100),
    product_category_title VARCHAR(100),
    card_type VARCHAR(50),
    card_type_title VARCHAR(50),
    card_level VARCHAR(50),
    card_level_title VARCHAR(50),
    energy_type VARCHAR(50),
    energy_type_title VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER DEFAULT 0
);

-- Create indexes for frequently queried fields
CREATE INDEX idx_card_type ON cards(card_type);
CREATE INDEX idx_energy_type ON cards(energy_type);
CREATE INDEX idx_rarity ON cards(rarity);
CREATE INDEX idx_title ON cards(title);
CREATE INDEX idx_card_no ON cards(card_no);

