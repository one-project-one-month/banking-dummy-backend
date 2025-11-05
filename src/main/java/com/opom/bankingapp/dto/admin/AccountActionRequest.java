package com.opom.bankingapp.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AccountActionRequest(
        @NotNull(message = "Action is required")
        @Schema(description = "The status action to apply to the user account.", example = "APPROVE")
        ActionType action
) {}
