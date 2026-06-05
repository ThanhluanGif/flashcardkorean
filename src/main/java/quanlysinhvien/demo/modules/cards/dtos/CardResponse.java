package quanlysinhvien.demo.modules.cards.dtos;

import lombok.Builder;
import lombok.Data;
import quanlysinhvien.demo.modules.cards.entities.Card;

import java.time.LocalDateTime;

@Data
@Builder
public class CardResponse {
    private Long id;
    private String front;
    private String back;
    private String example;
    private Long deckId;
    private Card.CardStatus status;
    private LocalDateTime nextReviewDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
