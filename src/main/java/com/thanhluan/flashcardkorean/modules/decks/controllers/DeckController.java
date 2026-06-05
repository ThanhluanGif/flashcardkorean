package com.thanhluan.flashcardkorean.modules.decks.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.thanhluan.flashcardkorean.modules.decks.dtos.DeckRequest;
import com.thanhluan.flashcardkorean.modules.decks.dtos.DeckResponse;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;
import com.thanhluan.flashcardkorean.modules.decks.services.DeckService;
import com.thanhluan.flashcardkorean.modules.users.entities.User;
import com.thanhluan.flashcardkorean.modules.users.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    // Tạo Deck mới cho user hiện tại (lấy từ JWT token)
    @PostMapping
    public ResponseEntity<DeckResponse> createDeck(@Valid @RequestBody DeckRequest request) {
        Long userId = getCurrentUserId();
        Deck deck = Deck.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        Deck savedDeck = deckService.createDeck(userId, deck);
        return new ResponseEntity<>(mapToResponse(savedDeck), HttpStatus.CREATED);
    }

    // Lấy danh sách Deck của user hiện tại
    @GetMapping("/my-decks")
    public ResponseEntity<List<DeckResponse>> getMyDecks() {
        Long userId = getCurrentUserId();
        List<DeckResponse> responses = deckService.getDecksByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy chi tiết Deck
    @GetMapping("/{deckId}")
    public ResponseEntity<?> getDeckById(@PathVariable Long deckId) {
        Deck deck = deckService.getDeckById(deckId);
        
        // Bảo mật bổ sung: Kiểm tra xem deck có thuộc về user hiện tại không
        if (!deck.getUser().getId().equals(getCurrentUserId())) {
            return new ResponseEntity<>("Bạn không có quyền truy cập bộ thẻ này", HttpStatus.FORBIDDEN);
        }
        
        return ResponseEntity.ok(mapToResponse(deck));
    }

    // Cập nhật Deck
    @PutMapping("/{deckId}")
    public ResponseEntity<?> updateDeck(@PathVariable Long deckId, @Valid @RequestBody DeckRequest request) {
        Deck deck = deckService.getDeckById(deckId);
        if (!deck.getUser().getId().equals(getCurrentUserId())) {
            return new ResponseEntity<>("Bạn không có quyền chỉnh sửa bộ thẻ này", HttpStatus.FORBIDDEN);
        }
        
        Deck updatedDeck = Deck.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
                
        Deck savedDeck = deckService.updateDeck(deckId, updatedDeck);
        return ResponseEntity.ok(mapToResponse(savedDeck));
    }

    // Xóa Deck
    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long deckId) {
        Deck deck = deckService.getDeckById(deckId);
        if (!deck.getUser().getId().equals(getCurrentUserId())) {
            return new ResponseEntity<>("Bạn không có quyền xóa bộ thẻ này", HttpStatus.FORBIDDEN);
        }
        deckService.deleteDeck(deckId);
        return ResponseEntity.noContent().build();
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
