package com.thanhluan.flashcardkorean.modules.cards.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thanhluan.flashcardkorean.modules.cards.dtos.CardRequest;
import com.thanhluan.flashcardkorean.modules.cards.dtos.CardResponse;
import com.thanhluan.flashcardkorean.modules.cards.dtos.CardReviewRequest;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;
import com.thanhluan.flashcardkorean.modules.cards.services.CardService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // Tạo Card mới trong một Deck
    @PostMapping("/deck/{deckId}")
    public ResponseEntity<CardResponse> createCard(@PathVariable Long deckId, @Valid @RequestBody CardRequest request) {
        Card card = Card.builder()
                .front(request.getFront())
                .back(request.getBack())
                .example(request.getExample())
                .build();

        Card savedCard = cardService.createCard(deckId, card);
        return new ResponseEntity<>(mapToResponse(savedCard), HttpStatus.CREATED);
    }

    // Lấy toàn bộ Card của một Deck
    @GetMapping("/deck/{deckId}")
    public ResponseEntity<List<CardResponse>> getCardsByDeckId(@PathVariable Long deckId) {
        List<CardResponse> responses = cardService.getCardsByDeckId(deckId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy danh sách Card CẦN ÔN TẬP hôm nay của một Deck
    @GetMapping("/deck/{deckId}/review")
    public ResponseEntity<List<CardResponse>> getCardsToReview(@PathVariable Long deckId) {
        List<CardResponse> responses = cardService.getCardsToReview(deckId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Xử lý gửi kết quả ôn tập một Card (Spaced Repetition)
    @PostMapping("/{cardId}/review")
    public ResponseEntity<CardResponse> reviewCard(@PathVariable Long cardId, @Valid @RequestBody CardReviewRequest request) {
        Card reviewedCard = cardService.reviewCard(cardId, request.getGrade());
        return ResponseEntity.ok(mapToResponse(reviewedCard));
    }

    // Cập nhật Card
    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long cardId, @Valid @RequestBody CardRequest request) {
        Card card = Card.builder()
                .front(request.getFront())
                .back(request.getBack())
                .example(request.getExample())
                .build();
        
        Card updatedCard = cardService.updateCard(cardId, card);
        return ResponseEntity.ok(mapToResponse(updatedCard));
    }

    // Xóa một Card
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    // Helper method mapping Entity -> DTO
    private CardResponse mapToResponse(Card card) {
        return CardResponse.builder()
                .id(card.getId())
                .front(card.getFront())
                .back(card.getBack())
                .example(card.getExample())
                .deckId(card.getDeck().getId())
                .status(card.getStatus())
                .nextReviewDate(card.getNextReviewDate())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .build();
    }
}
