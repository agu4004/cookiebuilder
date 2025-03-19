document.addEventListener("DOMContentLoaded", () => {
  console.log("DOM content loaded, initializing...");
  
  // Initialize tooltips
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  if (typeof bootstrap !== "undefined") {
    var tooltipList = tooltipTriggerList.map((tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl))
  }

  // Filter badge selection
  const energyFilters = document.querySelectorAll(".energy-filter")
  const rarityFilters = document.querySelectorAll(".rarity-filter")

  // Helper function to handle filter selection and auto-apply
  function handleFilterSelection(filterElements) {
    filterElements.forEach((filter) => {
      filter.addEventListener("click", function (e) {
        e.preventDefault(); // Prevent any default behavior
        
        // Remove active class from all filters in this group
        filterElements.forEach((f) => f.classList.remove("active"))
        
        // Add active class to clicked filter
        this.classList.add("active")
        
        // Auto-apply filters when any filter is clicked
        applyFilters()
      })
    })
  }

  // Initialize filter selections
  handleFilterSelection(energyFilters)
  handleFilterSelection(rarityFilters)

  // Set first option as active by default only if no other filter is active
  function setDefaultActiveFilters() {
    // For energy types
    if (!Array.from(energyFilters).some(f => f.classList.contains('active')) && energyFilters.length > 0) {
      energyFilters[0].classList.add("active")
    }
    
    // For rarities
    if (!Array.from(rarityFilters).some(f => f.classList.contains('active')) && rarityFilters.length > 0) {
      rarityFilters[0].classList.add("active")
    }
  }
  
  // Call this function to set default active filters
  setDefaultActiveFilters()

  // Function to apply filters
  function applyFilters() {
    const selectedEnergy = document.querySelector(".energy-filter.active")?.getAttribute("data-energy") || ""
    const selectedRarity = document.querySelector(".rarity-filter.active")?.getAttribute("data-rarity") || ""
    const activeTab = document.querySelector(".nav-link.active")
    const cardType = activeTab ? activeTab.textContent.trim() : ""

    console.log("Applying filters:", { 
      energy: selectedEnergy, 
      rarity: selectedRarity,
      cardType: cardType 
    })
    
    let visibleCount = 0
    let totalCount = 0
    
    // Get all card containers
    const cardContainers = document.querySelectorAll(".card-container")
    console.log(`Found ${cardContainers.length} total card containers`)
    
    // Apply filters
    cardContainers.forEach((container, index) => {
      const card = container.querySelector(".tcg-card")
      if (!card) {
        console.warn(`Card container at index ${index} has no tcg-card element`)
        return
      }
      
      totalCount++
      
      // Get card attributes directly from data attributes
      let cardEnergy = card.dataset.energyType || ""
      let cardGrade = card.dataset.rarity || ""
      
      // Check energy and rarity match
      const energyMatch = !selectedEnergy || 
        (selectedEnergy === "Mix" ? 
          (cardEnergy === "Mix" || cardEnergy.toLowerCase().includes("mix")) : 
          (cardEnergy || "").toLowerCase() === selectedEnergy.toLowerCase())
      const rarityMatch = !selectedRarity || (cardGrade || "").toLowerCase() === selectedRarity.toLowerCase()
      
      // Show/hide based on filters
      if (energyMatch && rarityMatch) {
        container.classList.remove("hidden")
        visibleCount++
      } else {
        container.classList.add("hidden")
      }
    });
    
    console.log(`Filtering results: ${visibleCount} of ${totalCount} cards visible`)
  }
  
  // Function to update the deck display with the current deck state
  async function refreshDeckDisplay() {
    try {
      console.log("Refreshing deck display...");
      const response = await fetch('/api/deck');
      
      console.log(`Deck API response status: ${response.status}`);
      
      if (response.ok) {
        const deck = await response.json();
        console.log("Raw deck API response:", deck);
        
        // Check if we have a valid deck object
        if (!deck) {
          console.error("Received null or undefined deck from API");
          updateEmptyDeckDisplay();
          return;
        }
        
        // Check for required properties
        console.log("Deck properties:", {
          size: deck.size,
          hasCardsArray: Array.isArray(deck.cards),
          cardsLength: deck.cards?.length || 0,
          hasCardCounts: !!deck.cardCounts,
          cardCountsSize: deck.cardCounts ? Object.keys(deck.cardCounts).length : 0
        });
        
        // Fix any issues with the deck data
        if (!deck.cards) {
          console.warn("Deck is missing cards array, initializing empty array");
          deck.cards = [];
        }
        
        if (!deck.cardCounts) {
          console.warn("Deck is missing cardCounts object, initializing empty object");
          deck.cardCounts = {};
        }
        
        // Preload card images to avoid sizing issues
        if (deck.cards && deck.cards.length > 0) {
          // Create array of image URLs
          const imageUrls = [];
          for (const cardName in deck.cardCounts) {
            if (deck.cardCounts[cardName] && deck.cardCounts[cardName].card) {
              imageUrls.push(deck.cardCounts[cardName].card.cardImage);
            }
          }
          
          // If we have images to preload, do that first
          if (imageUrls.length > 0) {
            try {
              await preloadImages(imageUrls);
            } catch (err) {
              console.warn("Some images failed to preload:", err);
            }
          }
          
          console.log("Deck has cards, updating display");
          updateDeckDisplay(deck);
        } else {
          console.log("Empty deck (no cards), showing empty message");
          updateEmptyDeckDisplay();
        }
      } else {
        console.error("Failed to fetch deck:", response.statusText);
        // Try to parse error response
        try {
          const errorText = await response.text();
          console.error("Error details:", errorText);
        } catch (e) {
          console.error("Couldn't read error details");
        }
      }
    } catch (error) {
      console.error("Error refreshing deck:", error);
    }
  }

  // Function to preload images
  function preloadImages(urls) {
    if (!urls || urls.length === 0) return Promise.resolve();
    
    const promises = urls.map(url => {
      return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => resolve(url);
        img.onerror = () => reject(url);
        img.src = url;
      });
    });
    
    return Promise.all(promises);
  }

  // Function to update display when deck is empty
  function updateEmptyDeckDisplay() {
    // Update deck size
    const deckSizeElement = document.getElementById("deck-size");
    if (deckSizeElement) {
      deckSizeElement.textContent = "0/60";
      // Remove color classes
      deckSizeElement.className = 'badge bg-primary';
    }
    
    // Find the card body element where deck content should be displayed
    let cardBody = document.querySelector('.card-body');
    const deckDisplaySections = document.querySelectorAll('.card-body');
    
    // Try to find the deck display section
    for (const section of deckDisplaySections) {
      if (section.textContent.includes('Your Deck')) {
        cardBody = section;
        break;
      }
    }
    
    if (!cardBody) {
      console.error("Could not find deck display section");
      return;
    }
    
    // Get or create the empty deck message element
    let emptyDeckMessage = document.getElementById("empty-deck-message");
    if (!emptyDeckMessage) {
      emptyDeckMessage = document.createElement('div');
      emptyDeckMessage.id = 'empty-deck-message';
      emptyDeckMessage.className = 'text-center py-4 text-muted';
      emptyDeckMessage.innerHTML = '<p>Your deck is empty. Add cards to get started!</p><p class="small text-muted">Deck limits: Maximum 60 cards total, up to 4 copies of each card.</p>';
      
      // Insert after the title
      const title = cardBody.querySelector('h2');
      if (title) {
        title.after(emptyDeckMessage);
      } else {
        cardBody.appendChild(emptyDeckMessage);
      }
    }
    
    // Show the empty deck message
    emptyDeckMessage.style.display = "block";
    
    // Clear the deck list if it exists
    const deckList = document.getElementById("deck-list");
    if (deckList) {
      deckList.innerHTML = '';
    }
  }

  function updateDeckDisplay(deck) {
    console.log("Updating deck display with deck:", deck);
    
    if (!deck) {
      console.error("updateDeckDisplay called with null or undefined deck");
      updateEmptyDeckDisplay();
      return;
    }
    
    // Update deck size with limit indicator but no color changes
    const deckSizeElement = document.getElementById("deck-size");
    if (deckSizeElement) {
      // Show deck size with the limit
      deckSizeElement.textContent = `${deck.size}/60`;
      // Use standard badge class without color indicators
      deckSizeElement.className = 'badge bg-primary';
    } else {
      console.warn("Deck size element not found");
    }
    
    // If deck is empty, show empty message and return
    if (!deck.cards || deck.cards.length === 0) {
      console.log("Deck has no cards, showing empty message");
      updateEmptyDeckDisplay();
      return;
    }
    
    // Find the card body element where deck content should be displayed
    let cardBody = document.querySelector('.card-body');
    const deckDisplaySections = document.querySelectorAll('.card-body');
    
    // Try to find the deck display section
    for (const section of deckDisplaySections) {
      if (section.textContent.includes('Your Deck')) {
        cardBody = section;
        break;
      }
    }
    
    if (!cardBody) {
      console.error("Could not find deck display section");
      return;
    }
    
    console.log("Found card body for deck display:", cardBody);
    
    // Get or create the deck list and empty message elements
    let emptyDeckMessage = document.getElementById("empty-deck-message");
    let deckList = document.getElementById("deck-list");
    
    // Create elements if they don't exist
    if (!emptyDeckMessage) {
      console.log("Creating empty deck message element");
      emptyDeckMessage = document.createElement('div');
      emptyDeckMessage.id = 'empty-deck-message';
      emptyDeckMessage.className = 'text-center py-4 text-muted';
      emptyDeckMessage.innerHTML = '<p>Your deck is empty. Add cards to get started!</p>';
      
      // Insert after the title
      const title = cardBody.querySelector('h2');
      if (title) {
        title.after(emptyDeckMessage);
      } else {
        cardBody.appendChild(emptyDeckMessage);
      }
    }
    
    if (!deckList) {
      console.log("Creating deck list element");
      deckList = document.createElement('div');
      deckList.id = 'deck-list';
      
      // Determine if we should use grid or list layout based on page
      if (window.location.pathname.includes('search') || window.location.pathname.includes('filter')) {
        deckList.className = 'row row-cols-1 row-cols-md-3 g-3';
      } else {
        deckList.className = 'deck-list';
      }
      
      // Insert after empty message or title
      if (emptyDeckMessage) {
        emptyDeckMessage.after(deckList);
      } else {
        const title = cardBody.querySelector('h2');
        if (title) {
          title.after(deckList);
        } else {
          cardBody.appendChild(deckList);
        }
      }
    }
    
    // Hide the empty deck message
    emptyDeckMessage.style.display = "none";
    console.log("Hiding empty deck message and showing deck list");
    
    // Prepare categories for different levels
    const level1Cards = [];
    const level2Cards = [];
    const level3Cards = [];
    const notCookiesCards = [];
    const flipCards = []; // New array for flip cards
    
    // Get card counts from the deck object
    const cardCounts = deck.cardCounts || {};
    console.log("Processing cards:", Object.keys(cardCounts).length, "unique cards");
    
    // Create a map to store card indices
    let cardIndexMap = {};
    
    // Map card names to their indices in the cards array
    const cards = deck.cards || [];
    for (let i = 0; i < cards.length; i++) {
      const card = cards[i];
      if (!card || !card.name) {
        console.warn("Found invalid card at index", i, card);
        continue;
      }
      
      const cardName = card.name;
      
      if (!cardIndexMap[cardName]) {
        cardIndexMap[cardName] = [];
      }
      
      cardIndexMap[cardName].push(i);
    }
    
    console.log("Card index map created with", Object.keys(cardIndexMap).length, "entries");
    
    // Count flip cards to enforce limit
    let flipCardCount = 0;
    
    // Categorize cards by level
    for (const cardName in cardCounts) {
      const cardInfo = cardCounts[cardName];
      const card = cardInfo.card;
      const count = cardInfo.count;
      
      if (!card) {
        console.warn("Invalid card info for", cardName, cardInfo);
        continue;
      }
      
      const indices = cardIndexMap[cardName] || [];
      const firstIndex = indices.length > 0 ? indices[0] : 0;
      
      // Add card to the appropriate level category
      const cardData = {
        card,
        count,
        firstIndex
      };
      
      // Check if card is a flip card - improved detection
      // Debug the card description to see what's actually in it
      console.log(`Card "${card.title}" description:`, card.field_cardDesc);
      
      // More thorough check for FLIP - account for missing or different structured tags
      const isFlipCard = card.field_cardDesc && (
        card.field_cardDesc.includes("<strong>FLIP</strong>") || 
        card.field_cardDesc.includes("<STRONG>FLIP</STRONG>") ||
        card.field_cardDesc.includes("FLIP") || 
        (card.cardType && card.cardType.toUpperCase().includes("FLIP"))
      );
      
      // Log whether this card was detected as a flip card
      console.log(`Card "${card.title}" is ${isFlipCard ? 'a flip card' : 'not a flip card'}`);
      
      // Add to flip card category if it's a flip card, otherwise add to appropriate level category
      if (isFlipCard) {
        flipCardCount += count;
        flipCards.push(cardData);
      } else if (card.cardLevel === 'level1') {
        level1Cards.push(cardData);
      } else if (card.cardLevel === 'level2') {
        level2Cards.push(cardData);
      } else if (card.cardLevel === 'level3') {
        level3Cards.push(cardData);
      } else {
        notCookiesCards.push(cardData);
      }
    }
    
    // Determine if we're using grid or list layout based on deck list class
    const isGridLayout = deckList.classList.contains('row');
    
    // Build HTML for each section
    let deckListHTML = '';
    
    // Add flip card limit indicator
    deckListHTML += `
      <div class="flip-card-counter mb-2">
        <span class="badge ${flipCardCount > 16 ? 'bg-danger' : 'bg-info'}">
          Flip Cards: ${flipCardCount}/16
        </span>
        ${flipCardCount > 16 ? '<span class="text-danger ms-2 small">Limit exceeded!</span>' : ''}
      </div>
    `;
    
    // Helper function to generate section HTML
    function generateSectionHTML(title, cards, sectionClass) {
      if (cards.length === 0) return '';
      
      let html = `
        <div class="deck-section ${sectionClass} mb-3">
          <h5 class="deck-section-title">${title}</h5>
          <div class="deck-section-content">
      `;
      
      for (const cardData of cards) {
        const card = cardData.card;
        const count = cardData.count;
        const firstIndex = cardData.firstIndex;
        
        // Add count badge without color indicators - always use bg-info
        const countBadge = `<span class="badge bg-info card-count-badge" title="${count}/4 copies">${count > 1 ? `×${count}` : ''}</span>`;
        
        // Create a consistent grid card layout with overlay similar to gallery cards
        html += `
          <div class="deck-card-item">
            <div class="tcg-card">
              <img src="${card.cardImage}" alt="${card.title}" class="preloaded-image" onload="this.classList.add('image-loaded')">
              <div class="card-overlay">
                <div class="card-info">
                  <h5>${card.title}</h5>
                  ${card.cardLevelTitle ? 
                    `<p class="mb-1">Level: <span>${card.cardLevelTitle}</span></p>` : ''}
                  <div class="d-flex justify-content-center gap-2">
                    <form action="/deck/remove/${firstIndex}" method="post" class="remove-card-form">
                      <button type="submit" class="btn btn-primary btn-sm rounded-circle">
                        <i class="bi bi-trash"></i>
                      </button>
                    </form>
                    <button type="button" class="btn btn-sm btn-fullsize rounded-pill"
                            onclick="openCardPopup(this)" 
                            data-card-image="${card.cardImage}">
                      <i class="bi bi-fullscreen"></i> <span>Full Size</span>
                    </button>
                  </div>
                </div>
              </div>
              ${count > 1 ? `<div class="position-absolute bottom-0 end-0">${countBadge}</div>` : ''}
            </div>
          </div>
        `;
      }
      
      html += `
          </div>
        </div>
      `;
      
      return html;
    }
    
    // Generate HTML for flip card section first
    deckListHTML += generateSectionHTML('Flip Cards', flipCards, 'flip-card-section');
    
    // Generate HTML for each level section
    deckListHTML += generateSectionHTML('Level 1', level1Cards, 'level-1-section');
    deckListHTML += generateSectionHTML('Level 2', level2Cards, 'level-2-section');
    deckListHTML += generateSectionHTML('Level 3', level3Cards, 'level-3-section');
    deckListHTML += generateSectionHTML('Not Cookies', notCookiesCards, 'not-cookies-section');
    
    // Update the deck list HTML
    deckList.innerHTML = deckListHTML;
    deckList.style.display = "block";
    
    // Attach event listeners to remove buttons
    attachRemoveCardEvents();
    
    // Dispatch event that deck has been updated
    document.dispatchEvent(new CustomEvent('deckUpdated'));
  }

  // Function to attach event listeners to remove card buttons
  function attachRemoveCardEvents() {
    document.querySelectorAll('form.remove-card-form').forEach(form => {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const index = form.action.split('/').pop();
        const submitButton = form.querySelector('button[type="submit"]');
        const originalButtonHTML = submitButton.innerHTML;
        
        try {
          // Change button appearance to show loading
          submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';
          submitButton.disabled = true;
          
          const response = await fetch(`/api/deck/remove/${index}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json'
            }
          });
          
          if (response.ok) {
            // Show success feedback
            submitButton.innerHTML = '<i class="bi bi-check"></i>';
            
            // Refresh the deck display
            await refreshDeckDisplay();
          } else {
            submitButton.innerHTML = '<i class="bi bi-x"></i>';
            console.error('Error removing card:', await response.text());
          }
        } catch (error) {
          submitButton.innerHTML = '<i class="bi bi-x"></i>';
          console.error('Error removing card:', error);
        }
      });
    });
  }

  // Function to attach event listeners to all add card forms
  function attachAddCardEvents() {
    // Find all add-card forms across the application
    const addCardForms = document.querySelectorAll('form[action^="/deck/add/"]');
    
    console.log("Found add card forms:", addCardForms.length);
    
    addCardForms.forEach((form, index) => {
      console.log(`Attaching listener to form #${index}:`, form.action);
      
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const cardId = form.action.split('/').pop();
        console.log(`Adding card with ID: ${cardId}`);
        
        const submitButton = form.querySelector('button[type="submit"]');
        
        try {
          // First, check if we've already reached the maximum deck size or card copies
          // Get current deck data
          const deckResponse = await fetch('/api/deck');
          
          if (!deckResponse.ok) {
            console.error('Error fetching deck data:', deckResponse.statusText);
            return;
          }
          
          const currentDeck = await deckResponse.json();
          
          // Check deck size limit (60 cards)
          if (currentDeck.size >= 60) {
            // Show notification to user that deck is full
            showNotification('Deck size limit reached! Your deck already has 60 cards.', 'warning');
            console.log('Cannot add card: Deck size limit (60) reached');
            return;
          }
          
          // Find the card being added in the current deck to check copy limit
          // We need to find which card is being added by its ID
          const cardToAdd = await fetch(`/api/cards/${cardId}`).then(res => res.ok ? res.json() : null);
          
          if (cardToAdd) {
            console.log("Card to add:", cardToAdd);
            
            // Improved flip card detection
            const isFlipCard = cardToAdd.field_cardDesc && (
              cardToAdd.field_cardDesc.includes("<strong>FLIP</strong>") || 
              cardToAdd.field_cardDesc.includes("<STRONG>FLIP</STRONG>") ||
              cardToAdd.field_cardDesc.includes("FLIP") || 
              (cardToAdd.cardType && cardToAdd.cardType.toUpperCase().includes("FLIP"))
            );
            
            console.log(`Card "${cardToAdd.title}" is ${isFlipCard ? 'a flip card' : 'not a flip card'}`);
            
            // Count existing flip cards in the deck
            if (isFlipCard) {
              let currentFlipCount = 0;
              
              // Count flip cards in the deck - improved detection
              for (const cardName in currentDeck.cardCounts) {
                const cardInfo = currentDeck.cardCounts[cardName];
                if (cardInfo && cardInfo.card && cardInfo.card.field_cardDesc && (
                  cardInfo.card.field_cardDesc.includes("<strong>FLIP</strong>") ||
                  cardInfo.card.field_cardDesc.includes("<STRONG>FLIP</STRONG>") ||
                  cardInfo.card.field_cardDesc.includes("FLIP") ||
                  (cardInfo.card.cardType && cardInfo.card.cardType.toUpperCase().includes("FLIP"))
                )) {
                  currentFlipCount += cardInfo.count;
                }
              }
              
              console.log("Current flip card count:", currentFlipCount);
              
              // Check if adding this card would exceed the flip card limit
              if (currentFlipCount >= 16) {
                // Show notification about flip card limit
                showNotification('Flip card limit reached! Your deck already has 16 flip cards.', 'warning');
                console.log('Cannot add card: Flip card limit (16) reached');
                return;
              }
            }
            
            // Check if we already have 4 copies of this card
            const cardName = cardToAdd.name;
            const currentCount = currentDeck.cardCounts && currentDeck.cardCounts[cardName] 
              ? currentDeck.cardCounts[cardName].count 
              : 0;
            
            if (currentCount >= 4) {
              // Show notification to user that they've reached the copy limit
              showNotification(`You already have 4 copies of "${cardToAdd.title}" in your deck.`, 'warning');
              console.log(`Cannot add card: Maximum copies (4) of ${cardToAdd.title} reached`);
              return;
            }
          }
          
          // If we pass all the checks, proceed with adding the card
          // Change button appearance to show loading
          const originalButtonHTML = submitButton.innerHTML;
          submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>';
          submitButton.disabled = true;
          
          console.log(`Sending POST request to /api/deck/add/${cardId}`);
          const response = await fetch(`/api/deck/add/${cardId}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            }
          });
          
          console.log(`Response status: ${response.status}`);
          
          if (response.ok) {
            const deckData = await response.json();
            console.log("Card added successfully. Response data:", deckData);
            
            // Make sure the deck data is valid
            console.log("Deck size:", deckData.size);
            console.log("Cards array length:", deckData.cards?.length);
            console.log("First few cards:", deckData.cards?.slice(0, 3));
            console.log("Card counts:", deckData.cardCounts);
            
            // Important: Use the response data directly instead of making another API call
            if (deckData && deckData.cards && deckData.cards.length > 0) {
              console.log("Directly updating deck display with response data");
              updateDeckDisplay(deckData);
            } else {
              console.log("Empty deck data in response, showing empty message");
              updateEmptyDeckDisplay();
            }
            
            // Show success message
            showNotification('Card added to your deck!', 'success');
            
            // Revert button back to original state
            submitButton.innerHTML = originalButtonHTML;
            submitButton.disabled = false;
          } else {
            console.error('Error adding card:', await response.text());
            submitButton.innerHTML = originalButtonHTML;
            submitButton.disabled = false;
            showNotification('Failed to add card to deck.', 'danger');
          }
        } catch (error) {
          console.error('Error adding card:', error);
          submitButton.innerHTML = originalButtonHTML;
          submitButton.disabled = false;
          showNotification('An error occurred while adding the card.', 'danger');
        }
      });
    });
  }
  
  // Function to show notification messages to the user
  function showNotification(message, type = 'info') {
    // Create notification element if it doesn't exist
    let notificationContainer = document.getElementById('notification-container');
    
    if (!notificationContainer) {
      notificationContainer = document.createElement('div');
      notificationContainer.id = 'notification-container';
      notificationContainer.style.position = 'fixed';
      notificationContainer.style.top = '10px';
      notificationContainer.style.right = '10px';
      notificationContainer.style.zIndex = '9999';
      notificationContainer.style.width = '300px';
      document.body.appendChild(notificationContainer);
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show`;
    notification.role = 'alert';
    notification.innerHTML = `
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    `;
    
    // Add notification to container
    notificationContainer.appendChild(notification);
    
    // Remove notification after 5 seconds
    setTimeout(() => {
      notification.classList.remove('show');
      setTimeout(() => {
        notificationContainer.removeChild(notification);
      }, 300);
    }, 5000);
    
    // Add click event to close button
    const closeButton = notification.querySelector('.btn-close');
    if (closeButton) {
      closeButton.addEventListener('click', () => {
        notification.classList.remove('show');
        setTimeout(() => {
          if (notification.parentNode === notificationContainer) {
            notificationContainer.removeChild(notification);
          }
        }, 300);
      });
    }
  }
  
  // Clear filters button functionality
  const clearFiltersBtn = document.getElementById('clearFilters');
  if (clearFiltersBtn) {
    clearFiltersBtn.addEventListener('click', () => {
      energyFilters[0]?.classList.add('active');
      energyFilters.forEach((f, i) => {
        if (i > 0) f.classList.remove('active');
      });
      
      rarityFilters[0]?.classList.add('active');
      rarityFilters.forEach((f, i) => {
        if (i > 0) f.classList.remove('active');
      });
      
      applyFilters();
    });
  }
  
  // Initialize the page
  console.log("Initializing page");
  
  // Make sure deck list is properly set up for an empty state initially
  const deckList = document.getElementById("deck-list");
  if (deckList) {
    console.log("Found deck list element during initialization");
    
    // Make sure it's visible by default (will be hidden if empty in refreshDeckDisplay)
    deckList.style.display = "block";
  } else {
    console.log("No deck list element found during initialization");
  }
  
  // Attach events to add card forms
  attachAddCardEvents();
  
  // Perform initial filtering if filters exist
  if (energyFilters.length > 0 || rarityFilters.length > 0) {
    applyFilters();
  }
  
  // Initial update of deck display - critical for showing cards
  console.log("Performing initial deck refresh");
  refreshDeckDisplay();
  
  // Card popup functions
  window.openCardPopup = function(button) {
    const cardImage = button.getAttribute('data-card-image');
    const popupOverlay = document.getElementById('cardPopupOverlay');
    const popupImage = document.getElementById('cardPopupImage');
    
    // Set the image source
    popupImage.src = cardImage;
    
    // Show the popup with animation
    popupOverlay.classList.add('active');
    
    // Prevent body scrolling when popup is open
    document.body.style.overflow = 'hidden';
  };
  
  // Close button event listener
  const closeButton = document.getElementById('cardPopupClose');
  if (closeButton) {
    closeButton.addEventListener('click', closeCardPopup);
  }
  
  // Close popup when clicking on the overlay background
  const popupOverlay = document.getElementById('cardPopupOverlay');
  if (popupOverlay) {
    popupOverlay.addEventListener('click', function(event) {
      // Only close if clicking the overlay itself, not the content
      if (event.target === popupOverlay) {
        closeCardPopup();
      }
    });
  }
  
  // Function to close the popup
  function closeCardPopup() {
    const popupOverlay = document.getElementById('cardPopupOverlay');
    popupOverlay.classList.remove('active');
    
    // Re-enable body scrolling
    document.body.style.overflow = '';
  }
  
  // Add ESC key listener to close popup
  document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
      closeCardPopup();
    }
  });
  
  // Card image hover preview functionality
  const hoverPreview = document.getElementById('cardHoverPreview');
  const previewImage = document.getElementById('previewImage');
  
  // Function to handle mouseover on card images
  function setupCardHoverPreviews() {
    // Apply to all full size buttons instead of the entire card
    const fullSizeButtons = document.querySelectorAll('.btn-fullsize');
    
    fullSizeButtons.forEach(button => {
      // Get the image source from the button's data attribute
      const imgSrc = button.getAttribute('data-card-image');
      if (!imgSrc) return;
      
      button.addEventListener('mouseenter', function(e) {
        // Set the preview image source
        previewImage.src = imgSrc;
        
        // Show the preview
        hoverPreview.classList.add('active');
        
        // Position the preview initially
        updatePreviewPosition(e);
      });
      
      button.addEventListener('mousemove', updatePreviewPosition);
      
      button.addEventListener('mouseleave', function() {
        // Hide the preview
        hoverPreview.classList.remove('active');
      });
    });
  }
  
  // Update the position of the hover preview
  function updatePreviewPosition(e) {
    if (!hoverPreview) return;
    
    // Get cursor position
    const x = e.clientX;
    const y = e.clientY;
    
    // Get window dimensions
    const windowWidth = window.innerWidth;
    const windowHeight = window.innerHeight;
    
    // Preview dimensions
    const previewWidth = 300; // max-width from CSS
    const previewHeight = Math.min(window.innerHeight * 0.7, 450); // approximate height
    
    // Determine position
    let left, top;
    
    // Position horizontally - try to keep on screen
    if (x + previewWidth + 20 < windowWidth) {
      // Enough space to the right
      left = x + 20;
    } else {
      // Not enough space to the right, position to the left
      left = x - previewWidth - 20;
    }
    
    // Position vertically - center with cursor if possible
    top = y - (previewHeight / 2);
    
    // Keep within vertical bounds
    if (top < 10) top = 10;
    if (top + previewHeight > windowHeight - 10) {
      top = windowHeight - previewHeight - 10;
    }
    
    // Apply position
    hoverPreview.style.left = `${left}px`;
    hoverPreview.style.top = `${top}px`;
  }
  
  // Initialize hover previews after page load
  setupCardHoverPreviews();
  
  // Re-initialize hover previews when deck display is updated
  document.addEventListener('deckUpdated', setupCardHoverPreviews);
});