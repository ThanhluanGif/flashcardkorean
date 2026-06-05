package com.thanhluan.flashcardkorean.modules.cards.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CardReviewRequest {

    @Min(value = 0, message = "Äiá»ƒm sá»‘ khÃ´ng Ä‘Æ°á»£c nhá» hÆ¡n 0")
    @Max(value = 3, message = "Äiá»ƒm sá»‘ khÃ´ng Ä‘Æ°á»£c lá»›n hÆ¡n 3")
    private int grade; // 0 (Again), 1 (Hard), 2 (Good), 3 (Easy)
}
