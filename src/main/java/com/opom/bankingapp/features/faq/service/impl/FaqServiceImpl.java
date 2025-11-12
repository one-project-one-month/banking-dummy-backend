package com.opom.bankingapp.features.faq.service.impl;

import com.opom.bankingapp.dto.faq.*;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.features.faq.repository.FaqRepository;
import com.opom.bankingapp.features.faq.service.FaqService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;
    private final RestTemplate restTemplate;

    public FaqServiceImpl(FaqRepository faqRepository, RestTemplate restTemplate) {
        this.faqRepository = faqRepository;
        this.restTemplate = restTemplate;
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

    @Override
    public AskFaqResponse askQuestion(AskFaqRequest request) {
        final String CHATBOT_API_URL = "https://ai-banking-app-backend-chatbot.onrender.com/ask";

        final ExternalFaqRequest externalRequest = new ExternalFaqRequest(request.question());

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        final HttpEntity<ExternalFaqRequest> entity = new HttpEntity<>(externalRequest, headers);

        try {
            final ResponseEntity<AskFaqResponse> response = restTemplate.postForEntity(
                    CHATBOT_API_URL,
                    entity,
                    AskFaqResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return new AskFaqResponse(response.getBody().answer());
            } else {
                throw new RuntimeException("Failed to get answer from chatbot API: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calling chatbot API: " + e.getMessage(), e);
        }
    }
}
