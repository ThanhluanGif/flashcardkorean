package quanlysinhvien.demo.modules.decks.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quanlysinhvien.demo.modules.decks.entities.Deck;
import quanlysinhvien.demo.modules.decks.repositories.DeckRepository;
import quanlysinhvien.demo.modules.decks.services.DeckService;
import quanlysinhvien.demo.modules.users.entities.User;
import quanlysinhvien.demo.modules.users.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;

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
    public Deck getDeckById(Long deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
    }

    @Override
    public void deleteDeck(Long deckId) {
        if (!deckRepository.existsById(deckId)) {
            throw new RuntimeException("Deck not found");
        }
        deckRepository.deleteById(deckId);
    }
}
