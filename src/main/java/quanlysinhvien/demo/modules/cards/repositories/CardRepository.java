package quanlysinhvien.demo.modules.cards.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quanlysinhvien.demo.modules.cards.entities.Card;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByDeckId(Long deckId);
    List<Card> findByDeckIdAndStatus(Long deckId, Card.CardStatus status);
    List<Card> findByNextReviewDateBefore(LocalDateTime date);
}
