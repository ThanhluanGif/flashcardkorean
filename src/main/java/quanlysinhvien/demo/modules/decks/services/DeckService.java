package quanlysinhvien.demo.modules.decks.services;

import quanlysinhvien.demo.modules.decks.entities.Deck;

import java.util.List;

public interface DeckService {
    Deck createDeck(Long userId, Deck deck);
    List<Deck> getDecksByUserId(Long userId);
    Deck getDeckById(Long deckId);
    void deleteDeck(Long deckId);
}
