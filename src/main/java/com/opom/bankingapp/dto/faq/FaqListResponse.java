package com.opom.bankingapp.dto.faq;

import java.util.List;

public record FaqListResponse(
    List<FaqDetailResponse> faqs
) {}
