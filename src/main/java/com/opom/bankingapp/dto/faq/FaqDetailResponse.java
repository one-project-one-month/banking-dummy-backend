package com.opom.bankingapp.dto.faq;

import java.time.LocalDateTime;

public record FaqDetailResponse(
    Long id,
    String question,
    String answer,
    String status,
    FaqCategoryResponse category,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
