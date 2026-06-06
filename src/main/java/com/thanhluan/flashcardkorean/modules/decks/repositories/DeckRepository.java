package com.thanhluan.flashcardkorean.modules.decks.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUserId(Long userId);
    Page<Deck> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title, Pageable pageable);
    Page<Deck> findByIsPublicTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);
}
