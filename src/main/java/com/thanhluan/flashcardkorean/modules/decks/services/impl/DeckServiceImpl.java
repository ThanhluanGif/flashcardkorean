package com.thanhluan.flashcardkorean.modules.decks.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;
import com.thanhluan.flashcardkorean.modules.cards.repositories.CardRepository;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;
import com.thanhluan.flashcardkorean.modules.decks.repositories.DeckRepository;
import com.thanhluan.flashcardkorean.modules.decks.services.DeckService;
import com.thanhluan.flashcardkorean.modules.users.entities.User;
import com.thanhluan.flashcardkorean.modules.users.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Override
    public Deck createDeck(Long userId, Deck deck) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        deck.setUser(user);
        return deckRepository.save(deck);
    }

    @Override
    public List<Deck> getDecksByUserId(Long userId) {
        return deckRepository.findByUserId(userId);
    }

    @Override
    public Page<Deck> getDecksPaginated(Long userId, String keyword, Pageable pageable) {
        if (keyword == null) keyword = "";
        return deckRepository.findByUserIdAndTitleContainingIgnoreCase(userId, keyword, pageable);
    }

    @Override
    public Page<Deck> getPublicDecks(String keyword, Pageable pageable) {
        if (keyword == null) keyword = "";
        return deckRepository.findByIsPublicTrueAndTitleContainingIgnoreCase(keyword, pageable);
    }

    @Override
    @Transactional
    public Deck cloneDeck(Long deckId, Long userId) {
        Deck sourceDeck = getDeckById(deckId);
        if (!sourceDeck.isPublic()) {
            throw new RuntimeException("Chỉ có thể sao chép bộ thẻ công khai");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo Deck mới
        Deck newDeck = Deck.builder()
                .title(sourceDeck.getTitle() + " (Copy)")
                .description(sourceDeck.getDescription())
                .isPublic(false)
                .user(user)
                .build();
        
        Deck savedDeck = deckRepository.save(newDeck);

        // Sao chép tất cả các Card
        List<Card> newCards = sourceDeck.getCards().stream().map(c -> {
            return Card.builder()
                    .front(c.getFront())
                    .back(c.getBack())
                    .example(c.getExample())
                    .imageUrl(c.getImageUrl())
                    .audioUrl(c.getAudioUrl())
                    .deck(savedDeck)
                    .status(Card.CardStatus.NEW)
                    .nextReviewDate(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        cardRepository.saveAll(newCards);
        return savedDeck;
    }

    @Override
    public Deck getDeckById(Long deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
    }

    @Override
    public Deck updateDeck(Long deckId, Deck deckRequest) {
        Deck deck = getDeckById(deckId);
        deck.setTitle(deckRequest.getTitle());
        deck.setDescription(deckRequest.getDescription());
        deck.setPublic(deckRequest.isPublic());
        return deckRepository.save(deck);
    }

    @Override
    public void deleteDeck(Long deckId) {
        if (!deckRepository.existsById(deckId)) {
            throw new RuntimeException("Deck not found");
        }
        deckRepository.deleteById(deckId);
    }
}
