package com.thanhluan.flashcardkorean.modules.decks.services;

import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;

import java.util.List;

public interface DeckService {
    Deck createDeck(Long userId, Deck deck);
    List<Deck> getDecksByUserId(Long userId);
    Deck getDeckById(Long deckId);
    Deck updateDeck(Long deckId, Deck deck);
    void deleteDeck(Long deckId);
}
