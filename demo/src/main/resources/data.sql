-- Clear existing data
DELETE FROM cards;

-- Insert sample Cookie Run card data with updated rarities and energy types
INSERT INTO cards (
    id, element_id, title, artist_title, product_title, card_desc, 
    rarity, hp, card_no, grade, card_image, product_category, 
    product_category_title, card_type, card_type_title, card_level, 
    card_level_title, energy_type, energy_type_title
) VALUES 
(76370, 72216, 'Strawberry Cookie', 'NEZZBE', 'PROMOTION CARD', 
 'If this Cookie has fainted, take 1 card from your support area to your hand. Then, place 1 card from your hand in your support area as rested.<br /><br />《{G}{G}》 Deals 2 damage.',
 'Rare', '2', 'P-011', 'PROMOTION', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/P_011.png.webp',
 'promotion-card', 'PROMOTION CARD', 'cookie', 'Cookie', 'level2', '2', 'green', 'Green'),

(76371, 72217, 'Gingerbrave', 'NEZZBE', 'STARTER DECK', 
 'When this Cookie enters the field, draw 1 card.<br /><br />《{G}》 Deals 1 damage.',
 'Common', '2', 'SD-001', 'STARTER', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/SD_001.png.webp',
 'starter-deck', 'STARTER DECK', 'cookie', 'Cookie', 'level1', '1', 'green', 'Green'),

(76372, 72218, 'Wizard Cookie', 'NEZZBE', 'BOOSTER PACK', 
 'When this Cookie enters the field, if you have 3 or more cards in your hand, deals 1 damage to your opponent''s Cookie.<br /><br />《{R}{R}》 Deals 2 damage.',
 'Rare', '3', 'BP-023', 'RARE', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_023.png.webp',
 'booster-pack', 'BOOSTER PACK', 'cookie', 'Cookie', 'level2', '2', 'red', 'Red'),


(76373, 72219, 'Healing Staff', 'NEZZBE', 'BOOSTER PACK', 
 'Attach to a Cookie.<br /><br />At the end of your turn, heal 1 damage from the attached Cookie.',
 'Uncommon', '', 'BP-045', 'UNCOMMON', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_045.png.webp',
 'booster-pack', 'BOOSTER PACK', 'item', 'Item', '', '', 'blue', 'Blue'),

(76374, 72220, 'Energy Potion', 'NEZZBE', 'STARTER DECK', 
 'Draw 2 cards.',
 'Common', '', 'SD-030', 'STARTER', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/SD_030.png.webp',
 'starter-deck', 'STARTER DECK', 'spell', 'Spell', '', '', 'yellow', 'Yellow'),

(76375, 72221, 'Millennial Tree Cookie', 'NEZZBE', 'BOOSTER PACK', 
 'When this Cookie enters the field, you may search your deck for up to 2 Plant Cookies and add them to your hand. If you do, shuffle your deck.<br /><br />《{G}{G}{G}》 Deals 3 damage.',
 'Super Rare', '4', 'BP-001', 'SUPER RARE', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_001.png.webp',
 'booster-pack', 'BOOSTER PACK', 'cookie', 'Cookie', 'level3', '3', 'green', 'Green'),

(76376, 72222, 'Jelly Watch', 'NEZZBE', 'BOOSTER PACK', 
 'Attach to a Cookie.<br /><br />The attached Cookie''s attack costs {1} less to use.',
 'Uncommon', '', 'BP-050', 'UNCOMMON', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_050.png.webp',
 'booster-pack', 'BOOSTER PACK', 'item', 'Item', '', '', 'purple', 'Purple'),

(76377, 72223, 'Jelly Berry Orchard', 'NEZZBE', 'BOOSTER PACK', 
 'At the beginning of your turn, add {G} to your energy pool.',
 'Rare', '', 'BP-060', 'RARE', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_060.png.webp',
 'booster-pack', 'BOOSTER PACK', 'stage', 'Stage', '', '', 'green', 'Green'),

(76378, 72224, 'Dark Enchantress Cookie', 'NEZZBE', 'BOOSTER PACK', 
 'When this Cookie enters the field, destroy all Items attached to your opponent''s Cookies.<br /><br />《{P}{P}{P}》 Deals 4 damage.',
 'Ultra Rare', '5', 'BP-100', 'ULTRA RARE', 
 'https://assets.cookierunbraverse.com/braverse/images/card_webp/card/en/BP_100.png.webp',
 'booster-pack', 'BOOSTER PACK', 'cookie', 'Cookie', 'level3', '3', 'purple', 'Purple');

