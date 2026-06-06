package com.thanhluan.flashcardkorean.modules.cards.dtos;

import lombok.Builder;
import lombok.Data;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;

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
    private String imageUrl;
    private String audioUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
