package com.opom.bankingapp.dto.faq;

import jakarta.validation.constraints.NotBlank;

public record AskFaqRequest(
    @NotBlank(message = "Question cannot be blank")
    String question
) {}
