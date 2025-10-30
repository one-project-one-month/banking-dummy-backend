package com.opom.bankingapp.features.faq.service;

import com.opom.bankingapp.dto.faq.FaqDetailResponse;
import com.opom.bankingapp.dto.faq.FaqListResponse;
import com.opom.bankingapp.dto.faq.FaqRequest;

public interface FaqService {
    FaqDetailResponse createFaq(Long createdBy, FaqRequest request);
    FaqDetailResponse updateFaq(Long faqId, Long updatedBy, FaqRequest request);
    void deleteFaq(Long faqId);
    FaqDetailResponse getFaqById(Long faqId);
    FaqListResponse getAllFaqs();
}
