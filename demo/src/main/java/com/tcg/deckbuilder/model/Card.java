package com.tcg.deckbuilder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cards")
public class Card {
    
    @Id
    private Long id;
    
    @Column(name = "element_id")
    @JsonProperty("elementId")
    private Long elementId;
    
    @Column(nullable = false)
    @JsonProperty("title")
    private String title;
    
    @Column(name = "artist_title")
    @JsonProperty("field_artistTitle")
    private String artistTitle;
    
    @Column(name = "product_title")
    @JsonProperty("field_productTitle")
    private String productTitle;
    
    @Column(name = "card_desc", length = 2000)
    @JsonProperty("field_cardDesc")
    private String cardDesc;
    
    @Column(name = "rarity")
    @JsonProperty("field_rare_tzsrperf")
    private String rarity;
    
    @Column(name = "hp")
    @JsonProperty("field_hp_zbxcocvx")
    private String hp;
    
    @Column(name = "card_no")
    @JsonProperty("field_cardNo_suyeowsc")
    private String cardNo;
    
    @Column(name = "grade")
    @JsonProperty("field_grade")
    private String grade;
    
    @Column(name = "card_image")
    @JsonProperty("cardImage")
    private String cardImage;
    
    @Column(name = "product_category")
    @JsonProperty("field_productCategory")
    private String productCategory;
    
    @Column(name = "product_category_title")
    @JsonProperty("field_productCategoryTitle")
    private String productCategoryTitle;
    
    @Column(name = "card_type")
    @JsonProperty("cardType")
    private String cardType;
    
    @Column(name = "card_type_title")
    @JsonProperty("cardTypeTitle")
    private String cardTypeTitle;
    
    @Column(name = "card_level")
    @JsonProperty("cardLevel")
    private String cardLevel;
    
    @Column(name = "card_level_title")
    @JsonProperty("cardLevelTitle")
    private String cardLevelTitle;
    
    @Column(name = "energy_type")
    @JsonProperty("energyType")
    private String energyType;
    
    @Column(name = "energy_type_title")
    @JsonProperty("energyTypeTitle")
    private String energyTypeTitle;
    
    // Hibernate specific features
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    private Integer version;
    
    // Default constructor required by JPA
    public Card() {
    }

    // Constructor with essential fields
    public Card(Long id, String title, String cardDesc, String rarity, String cardImage) {
        this.id = id;
        this.title = title;
        this.cardDesc = cardDesc;
        this.rarity = rarity;
        this.cardImage = cardImage;
    }

    // Full constructor
    public Card(Long id, Long elementId, String title, String artistTitle, String productTitle, 
                String cardDesc, String rarity, String hp, String cardNo, String grade, 
                String cardImage, String productCategory, String productCategoryTitle, 
                String cardType, String cardTypeTitle, String cardLevel, String cardLevelTitle, 
                String energyType, String energyTypeTitle) {
        this.id = id;
        this.elementId = elementId;
        this.title = title;
        this.artistTitle = artistTitle;
        this.productTitle = productTitle;
        this.cardDesc = cardDesc;
        this.rarity = rarity;
        this.hp = hp;
        this.cardNo = cardNo;
        this.grade = grade;
        this.cardImage = cardImage;
        this.productCategory = productCategory;
        this.productCategoryTitle = productCategoryTitle;
        this.cardType = cardType;
        this.cardTypeTitle = cardTypeTitle;
        this.cardLevel = cardLevel;
        this.cardLevelTitle = cardLevelTitle;
        this.energyType = energyType;
        this.energyTypeTitle = energyTypeTitle;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistTitle() {
        return artistTitle;
    }

    public void setArtistTitle(String artistTitle) {
        this.artistTitle = artistTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategoryTitle() {
        return productCategoryTitle;
    }

    public void setProductCategoryTitle(String productCategoryTitle) {
        this.productCategoryTitle = productCategoryTitle;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeTitle() {
        return cardTypeTitle;
    }

    public void setCardTypeTitle(String cardTypeTitle) {
        this.cardTypeTitle = cardTypeTitle;
    }

    public String getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(String cardLevel) {
        this.cardLevel = cardLevel;
    }

    public String getCardLevelTitle() {
        return cardLevelTitle;
    }

    public void setCardLevelTitle(String cardLevelTitle) {
        this.cardLevelTitle = cardLevelTitle;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public String getEnergyTypeTitle() {
        return energyTypeTitle;
    }

    public void setEnergyTypeTitle(String energyTypeTitle) {
        this.energyTypeTitle = energyTypeTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardTypeTitle + '\'' +
                '}';
    }
    
    // Helper method to get the name (for compatibility with existing code)
    public String getName() {
        // Unique key for the card to allow proper counting in the deck
        // Use a combination of title and id to ensure uniqueness
        return this.id + ":" + this.title;
    }
    
    // Helper method to get the type (for compatibility with existing code)
    public String getType() {
        return cardTypeTitle != null ? cardTypeTitle : cardType;
    }
}

