package com.opom.bankingapp.dto.auth;

import com.opom.bankingapp.dto.common.OptionDto;
import java.util.List;

public record PersonalDetailsTemplateResponse(
    List<OptionDto> genderOptions,
    List<OptionDto> nationalityOptions
) {}
