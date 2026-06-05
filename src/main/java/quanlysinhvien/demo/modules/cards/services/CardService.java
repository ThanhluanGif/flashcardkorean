package quanlysinhvien.demo.modules.cards.services;

import quanlysinhvien.demo.modules.cards.entities.Card;

import java.util.List;

public interface CardService {
    Card createCard(Long deckId, Card card);
    List<Card> getCardsByDeckId(Long deckId);
    List<Card> getCardsToReview(Long deckId);
    Card reviewCard(Long cardId, int grade);
    void deleteCard(Long cardId);
}
