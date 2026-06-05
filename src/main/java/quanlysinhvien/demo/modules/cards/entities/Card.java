package quanlysinhvien.demo.modules.cards.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import quanlysinhvien.demo.modules.decks.entities.Deck;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String front; // Tiếng Hàn

    @Column(nullable = false, columnDefinition = "TEXT")
    private String back; // Nghĩa tiếng Việt/Anh

    @Column(columnDefinition = "TEXT")
    private String example; // Ví dụ minh họa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CardStatus status = CardStatus.NEW;

    @Column(name = "next_review_date")
    private LocalDateTime nextReviewDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum CardStatus {
        NEW, LEARNING, REVIEW, MASTERED
    }
}
