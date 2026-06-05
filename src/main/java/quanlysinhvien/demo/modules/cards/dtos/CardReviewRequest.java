package quanlysinhvien.demo.modules.cards.dtos;

import lombok.Data;

@Data
public class CardReviewRequest {
    private int grade; // 0 (Again), 1 (Hard), 2 (Good), 3 (Easy)
}
