package com.thanhluan.flashcardkorean.modules.decks.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;

import java.util.List;

public interface DeckService {
    Deck createDeck(Long userId, Deck deck);
    List<Deck> getDecksByUserId(Long userId);
    Page<Deck> getDecksPaginated(Long userId, String keyword, Pageable pageable);
    Page<Deck> getPublicDecks(String keyword, Pageable pageable);
    Deck cloneDeck(Long deckId, Long userId);
    Deck getDeckById(Long deckId);
    Deck updateDeck(Long deckId, Deck deck);
    void deleteDeck(Long deckId);
}
