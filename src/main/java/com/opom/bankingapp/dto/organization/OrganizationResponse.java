package com.opom.bankingapp.dto.organization;

import java.time.LocalDateTime;

public record OrganizationResponse(
    Long organizationId,
    String organizationName,
    String organizationAdmin,
    String adminEmail,
    Boolean status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long createdBy,
    Long updatedBy
) {}
