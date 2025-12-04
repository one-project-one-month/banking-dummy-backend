package com.opom.bankingapp.dto.admin;

import java.time.LocalDateTime;

public record UserAdminResponse(
    Long userId,
    String fullName,
    String emailAddress,
    String role,
    String organizationName,
    Integer status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
