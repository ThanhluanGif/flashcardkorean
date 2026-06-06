package com.thanhluan.flashcardkorean.modules.cards.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.thanhluan.flashcardkorean.modules.cards.dtos.UserStatsResponse;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;
import com.thanhluan.flashcardkorean.modules.cards.repositories.CardRepository;
import com.thanhluan.flashcardkorean.modules.cards.services.CardService;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;
import com.thanhluan.flashcardkorean.modules.decks.repositories.DeckRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @Override
    public Card createCard(Long deckId, Card card) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        card.setDeck(deck);
        card.setStatus(Card.CardStatus.NEW);
        card.setNextReviewDate(LocalDateTime.now());
        return cardRepository.save(card);
    }

    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        return cardRepository.findByDeckId(deckId);
    }

    @Override
    public Page<Card> getCardsPaginated(Long deckId, String keyword, Pageable pageable) {
        if (keyword == null) keyword = "";
        return cardRepository.findByDeckIdAndFrontContainingIgnoreCaseOrDeckIdAndBackContainingIgnoreCase(deckId, keyword, deckId, keyword, pageable);
    }

    @Override
    public List<Card> getCardsToReview(Long deckId) {
        LocalDateTime now = LocalDateTime.now();
        // Lấy tất cả card của deck có nextReviewDate <= now
        return cardRepository.findByDeckId(deckId).stream()
                .filter(c -> c.getNextReviewDate() != null && !c.getNextReviewDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public Card reviewCard(Long cardId, int grade) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        LocalDateTime now = LocalDateTime.now();

        // Thuật toán Spaced Repetition (Lặp lại ngắt quãng) cơ bản
        if (grade == 0) {
            card.setStatus(Card.CardStatus.LEARNING);
            card.setNextReviewDate(now.plusMinutes(10));
        } else {
            switch (card.getStatus()) {
                case NEW:
                    if (grade == 3) {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(4));
                    } else {
                        card.setStatus(Card.CardStatus.LEARNING);
                        card.setNextReviewDate(now.plusDays(1));
                    }
                    break;
                case LEARNING:
                    if (grade >= 2) {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(3));
                    } else {
                        card.setNextReviewDate(now.plusDays(1));
                    }
                    break;
                case REVIEW:
                    if (grade == 3) {
                        card.setStatus(Card.CardStatus.MASTERED);
                        card.setNextReviewDate(now.plusDays(14));
                    } else if (grade == 2) {
                        card.setNextReviewDate(now.plusDays(7));
                    } else {
                        card.setNextReviewDate(now.plusDays(3));
                    }
                    break;
                case MASTERED:
                    if (grade >= 2) {
                        card.setNextReviewDate(now.plusMonths(1));
                    } else {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(7));
                    }
                    break;
            }
        }

        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Long cardId, Card cardRequest) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setFront(cardRequest.getFront());
        card.setBack(cardRequest.getBack());
        card.setExample(cardRequest.getExample());
        card.setImageUrl(cardRequest.getImageUrl());
        card.setAudioUrl(cardRequest.getAudioUrl());
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new RuntimeException("Card not found");
        }
        cardRepository.deleteById(cardId);
    }

    @Override
    public UserStatsResponse getUserStats(Long userId) {
        long totalDecks = deckRepository.findByUserId(userId).size();
        long cardsDueToday = cardRepository.countByDeckUserIdAndNextReviewDateBefore(userId, LocalDateTime.now());
        
        Map<String, Long> statusCounts = new HashMap<>();
        long totalCards = 0;
        for (Card.CardStatus status : Card.CardStatus.values()) {
            long count = cardRepository.countByDeckUserIdAndStatus(userId, status);
            statusCounts.put(status.name(), count);
            totalCards += count;
        }

        return UserStatsResponse.builder()
                .totalDecks(totalDecks)
                .totalCards(totalCards)
                .cardsDueToday(cardsDueToday)
                .statusCounts(statusCounts)
                .build();
    }
}
