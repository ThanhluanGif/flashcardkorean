package com.thanhluan.flashcardkorean.modules.cards.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.thanhluan.flashcardkorean.modules.cards.dtos.UserStatsResponse;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;

import java.util.List;

public interface CardService {
    Card createCard(Long deckId, Card card);
    List<Card> getCardsByDeckId(Long deckId);
    Page<Card> getCardsPaginated(Long deckId, String keyword, Pageable pageable);
    List<Card> getCardsToReview(Long deckId);
    Card reviewCard(Long cardId, int grade);
    Card updateCard(Long cardId, Card card);
    void deleteCard(Long cardId);
    UserStatsResponse getUserStats(Long userId);
}
