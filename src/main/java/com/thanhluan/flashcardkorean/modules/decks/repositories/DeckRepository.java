package com.thanhluan.flashcardkorean.modules.decks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUserId(Long userId);
}
