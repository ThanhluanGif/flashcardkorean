package com.thanhluan.flashcardkorean.modules.decks.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeckRequest {

    @NotBlank(message = "Tên bộ thẻ (title) không được để trống")
    private String title;

    private String description;
}
