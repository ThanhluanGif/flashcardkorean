package quanlysinhvien.demo.modules.decks.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quanlysinhvien.demo.modules.decks.dtos.DeckRequest;
import quanlysinhvien.demo.modules.decks.dtos.DeckResponse;
import quanlysinhvien.demo.modules.decks.entities.Deck;
import quanlysinhvien.demo.modules.decks.services.DeckService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    // Tạo Deck mới cho một user (truyền userId qua query param tạm thời trước khi có Security)
    @PostMapping
    public ResponseEntity<?> createDeck(@RequestParam Long userId, @RequestBody DeckRequest request) {
        try {
            Deck deck = Deck.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .build();

            Deck savedDeck = deckService.createDeck(userId, deck);
            return new ResponseEntity<>(mapToResponse(savedDeck), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Lấy danh sách Deck của một User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeckResponse>> getDecksByUser(@PathVariable Long userId) {
        List<DeckResponse> responses = deckService.getDecksByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy chi tiết Deck
    @GetMapping("/{deckId}")
    public ResponseEntity<?> getDeckById(@PathVariable Long deckId) {
        try {
            Deck deck = deckService.getDeckById(deckId);
            return ResponseEntity.ok(mapToResponse(deck));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Xóa Deck
    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long deckId) {
        try {
            deckService.deleteDeck(deckId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Helper method mapping Entity -> DTO
    private DeckResponse mapToResponse(Deck deck) {
        return DeckResponse.builder()
                .id(deck.getId())
                .title(deck.getTitle())
                .description(deck.getDescription())
                .userId(deck.getUser().getId())
                .createdAt(deck.getCreatedAt())
                .updatedAt(deck.getUpdatedAt())
                .build();
    }
}
