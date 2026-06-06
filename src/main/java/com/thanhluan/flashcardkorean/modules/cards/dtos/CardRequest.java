package com.thanhluan.flashcardkorean.modules.cards.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardRequest {

    @NotBlank(message = "Mặt trước (front) không được để trống")
    private String front;

    @NotBlank(message = "Mặt sau (back) không được để trống")
    private String back;

    private String example;

    private String imageUrl;

    private String audioUrl;
}
