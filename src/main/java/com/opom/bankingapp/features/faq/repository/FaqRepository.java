package com.opom.bankingapp.features.faq.repository;

import com.opom.bankingapp.dto.faq.FaqRequest;
import com.opom.bankingapp.dto.faq.FaqDetailResponse;
import com.opom.bankingapp.dto.faq.FaqCategoryResponse;
import java.util.List;
import java.util.Optional;

public interface FaqRepository {
    Long save(Long createdBy, FaqRequest request);
    void update(Long faqId, Long updatedBy, FaqRequest request);
    void delete(Long faqId);
    Optional<FaqDetailResponse> findById(Long faqId);
    List<FaqDetailResponse> findAll();
    Optional<FaqCategoryResponse> findCategoryById(Integer categoryId);
}
