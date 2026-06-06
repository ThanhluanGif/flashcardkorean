package com.thanhluan.flashcardkorean.modules.cards.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserStatsResponse {
    private long totalCards;
    private long totalDecks;
    private long cardsDueToday;
    private Map<String, Long> statusCounts;
}
