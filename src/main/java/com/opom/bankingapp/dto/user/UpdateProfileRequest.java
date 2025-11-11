package com.opom.bankingapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record UpdateProfileRequest(
    @NotBlank(message = "Fullname cannot be blank")
    String fullname,

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate dateOfBirth,

    @NotNull(message = "Gender ID is required")
    Integer genderId,

    @NotNull(message = "Nationality ID is required")
    Integer nationalityId,

    @NotBlank(message = "Phone number cannot be blank")
    String phoneNumber,

    @NotBlank(message = "Address cannot be blank")
    String address
) {}
