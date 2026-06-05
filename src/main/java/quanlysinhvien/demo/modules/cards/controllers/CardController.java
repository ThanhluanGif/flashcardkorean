package quanlysinhvien.demo.modules.cards.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quanlysinhvien.demo.modules.cards.dtos.CardRequest;
import quanlysinhvien.demo.modules.cards.dtos.CardResponse;
import quanlysinhvien.demo.modules.cards.dtos.CardReviewRequest;
import quanlysinhvien.demo.modules.cards.entities.Card;
import quanlysinhvien.demo.modules.cards.services.CardService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // Tạo Card mới trong một Deck
    @PostMapping("/deck/{deckId}")
    public ResponseEntity<?> createCard(@PathVariable Long deckId, @RequestBody CardRequest request) {
        try {
            Card card = Card.builder()
                    .front(request.getFront())
                    .back(request.getBack())
                    .example(request.getExample())
                    .build();

            Card savedCard = cardService.createCard(deckId, card);
            return new ResponseEntity<>(mapToResponse(savedCard), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<?> reviewCard(@PathVariable Long cardId, @RequestBody CardReviewRequest request) {
        try {
            Card reviewedCard = cardService.reviewCard(cardId, request.getGrade());
            return ResponseEntity.ok(mapToResponse(reviewedCard));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Xóa một Card
    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        try {
            cardService.deleteCard(cardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
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
