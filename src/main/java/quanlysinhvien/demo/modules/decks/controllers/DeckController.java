package quanlysinhvien.demo.modules.decks.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import quanlysinhvien.demo.modules.decks.dtos.DeckRequest;
import quanlysinhvien.demo.modules.decks.dtos.DeckResponse;
import quanlysinhvien.demo.modules.decks.entities.Deck;
import quanlysinhvien.demo.modules.decks.services.DeckService;
import quanlysinhvien.demo.modules.users.entities.User;
import quanlysinhvien.demo.modules.users.repositories.UserRepository;

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
    public ResponseEntity<?> createDeck(@Valid @RequestBody DeckRequest request) {
        try {
            Long userId = getCurrentUserId();
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
        try {
            Deck deck = deckService.getDeckById(deckId);
            
            // Bảo mật bổ sung: Kiểm tra xem deck có thuộc về user hiện tại không
            if (!deck.getUser().getId().equals(getCurrentUserId())) {
                return new ResponseEntity<>("Bạn không có quyền truy cập bộ thẻ này", HttpStatus.FORBIDDEN);
            }
            
            return ResponseEntity.ok(mapToResponse(deck));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Xóa Deck
    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long deckId) {
        try {
            Deck deck = deckService.getDeckById(deckId);
            if (!deck.getUser().getId().equals(getCurrentUserId())) {
                return new ResponseEntity<>("Bạn không có quyền xóa bộ thẻ này", HttpStatus.FORBIDDEN);
            }
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
