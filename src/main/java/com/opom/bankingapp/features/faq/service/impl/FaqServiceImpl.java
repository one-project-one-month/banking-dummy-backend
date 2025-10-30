package com.opom.bankingapp.features.faq.service.impl;

import com.opom.bankingapp.dto.faq.FaqDetailResponse;
import com.opom.bankingapp.dto.faq.FaqListResponse;
import com.opom.bankingapp.dto.faq.FaqRequest;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.features.faq.repository.FaqRepository;
import com.opom.bankingapp.features.faq.service.FaqService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    public FaqServiceImpl(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    @Override
    @Transactional
    public FaqDetailResponse createFaq(Long createdBy, FaqRequest request) {
        faqRepository.findCategoryById(request.faqCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("FAQ Category not found with ID: " + request.faqCategoryId()));

        Long newId = faqRepository.save(createdBy, request);
        
        return faqRepository.findById(newId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created FAQ."));
    }

    @Override
    @Transactional
    public FaqDetailResponse updateFaq(Long faqId, Long updatedBy, FaqRequest request) {
        faqRepository.findById(faqId)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with ID: " + faqId));

        faqRepository.findCategoryById(request.faqCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("FAQ Category not found with ID: " + request.faqCategoryId()));
        
        faqRepository.update(faqId, updatedBy, request);
        
        return faqRepository.findById(faqId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated FAQ."));
    }

    @Override
    @Transactional
    public void deleteFaq(Long faqId) {
        faqRepository.delete(faqId);
    }

    @Override
    @Transactional(readOnly = true)
    public FaqDetailResponse getFaqById(Long faqId) {
        return faqRepository.findById(faqId)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found with ID: " + faqId));
    }

    @Override
    @Transactional(readOnly = true)
    public FaqListResponse getAllFaqs() {
        return new FaqListResponse(faqRepository.findAll());
    }
}
