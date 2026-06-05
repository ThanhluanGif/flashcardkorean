package quanlysinhvien.demo.modules.cards.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CardReviewRequest {

    @Min(value = 0, message = "Điểm số không được nhỏ hơn 0")
    @Max(value = 3, message = "Điểm số không được lớn hơn 3")
    private int grade; // 0 (Again), 1 (Hard), 2 (Good), 3 (Easy)
}
