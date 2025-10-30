package com.opom.bankingapp.dto.organization;

import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(
    @NotBlank String name,
    @NotBlank String shortcode,
    @NotBlank String address,
    @NotBlank String country
) {}
