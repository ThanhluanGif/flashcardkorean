package com.thanhluan.flashcardkorean.modules.cards.services;

import com.thanhluan.flashcardkorean.modules.cards.entities.Card;

import java.util.List;

public interface CardService {
    Card createCard(Long deckId, Card card);
    List<Card> getCardsByDeckId(Long deckId);
    List<Card> getCardsToReview(Long deckId);
    Card reviewCard(Long cardId, int grade);
    Card updateCard(Long cardId, Card card);
    void deleteCard(Long cardId);
}
