package com.opom.bankingapp.dto.faq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FaqRequest(
    @NotBlank(message = "Question is required") String question,
    @NotBlank(message = "Answer is required") String answer,
    @NotNull(message = "FAQ Category ID is required") Integer faqCategoryId
) {}
