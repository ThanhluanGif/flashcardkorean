package com.thanhluan.flashcardkorean.modules.cards.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.thanhluan.flashcardkorean.modules.cards.entities.Card;
import com.thanhluan.flashcardkorean.modules.cards.repositories.CardRepository;
import com.thanhluan.flashcardkorean.modules.cards.services.CardService;
import com.thanhluan.flashcardkorean.modules.decks.entities.Deck;
import com.thanhluan.flashcardkorean.modules.decks.repositories.DeckRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    @Override
    public Card createCard(Long deckId, Card card) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        card.setDeck(deck);
        card.setStatus(Card.CardStatus.NEW);
        card.setNextReviewDate(LocalDateTime.now());
        return cardRepository.save(card);
    }

    @Override
    public List<Card> getCardsByDeckId(Long deckId) {
        return cardRepository.findByDeckId(deckId);
    }

    @Override
    public List<Card> getCardsToReview(Long deckId) {
        LocalDateTime now = LocalDateTime.now();
        // Lấy tất cả card của deck có nextReviewDate <= now
        return cardRepository.findByDeckId(deckId).stream()
                .filter(c -> c.getNextReviewDate() != null && !c.getNextReviewDate().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public Card reviewCard(Long cardId, int grade) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        LocalDateTime now = LocalDateTime.now();

        // Thuật toán Spaced Repetition (Lặp lại ngắt quãng) cơ bản
        // grade: 0 (Quên/Again), 1 (Khó/Hard), 2 (Tốt/Good), 3 (Dễ/Easy)
        if (grade == 0) {
            card.setStatus(Card.CardStatus.LEARNING);
            card.setNextReviewDate(now.plusMinutes(10)); // Học lại liền sau 10 phút
        } else {
            switch (card.getStatus()) {
                case NEW:
                    if (grade == 3) {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(4)); // Thuộc luôn, ôn lại sau 4 ngày
                    } else {
                        card.setStatus(Card.CardStatus.LEARNING);
                        card.setNextReviewDate(now.plusDays(1)); // Khó/Tốt thì mai ôn lại
                    }
                    break;
                case LEARNING:
                    if (grade >= 2) {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(3)); // Học xong, 3 ngày nữa ôn
                    } else {
                        card.setNextReviewDate(now.plusDays(1)); // Vẫn khó, mai ôn
                    }
                    break;
                case REVIEW:
                    if (grade == 3) {
                        card.setStatus(Card.CardStatus.MASTERED);
                        card.setNextReviewDate(now.plusDays(14)); // Dễ quá -> Thành thạo
                    } else if (grade == 2) {
                        card.setNextReviewDate(now.plusDays(7)); // Bình thường -> 1 tuần sau
                    } else {
                        card.setNextReviewDate(now.plusDays(3)); // Hơi khó -> 3 ngày sau
                    }
                    break;
                case MASTERED:
                    if (grade >= 2) {
                        card.setNextReviewDate(now.plusMonths(1)); // Cứ 1 tháng ôn 1 lần
                    } else {
                        card.setStatus(Card.CardStatus.REVIEW);
                        card.setNextReviewDate(now.plusDays(7)); // Quên một chút, giáng cấp
                    }
                    break;
            }
        }

        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Long cardId, Card cardRequest) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        card.setFront(cardRequest.getFront());
        card.setBack(cardRequest.getBack());
        card.setExample(cardRequest.getExample());
        return cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new RuntimeException("Card not found");
        }
        cardRepository.deleteById(cardId);
    }
}
