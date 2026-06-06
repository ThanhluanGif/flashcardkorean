package com.thanhluan.flashcardkorean.modules.cards.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByDeckId(Long deckId);
    Page<Card> findByDeckIdAndFrontContainingIgnoreCaseOrDeckIdAndBackContainingIgnoreCase(Long deckId1, String front, Long deckId2, String back, Pageable pageable);
    List<Card> findByDeckIdAndStatus(Long deckId, Card.CardStatus status);
    List<Card> findByNextReviewDateBefore(LocalDateTime date);
    long countByDeckUserIdAndStatus(Long userId, Card.CardStatus status);
    long countByDeckUserIdAndNextReviewDateBefore(Long userId, LocalDateTime date);
}
