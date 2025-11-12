package com.opom.bankingapp.controller;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.faq.AskFaqRequest;
import com.opom.bankingapp.dto.faq.AskFaqResponse;
import com.opom.bankingapp.features.faq.service.FaqService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faqs")
public class PublicFaqController {

    private final FaqService faqService;

    public PublicFaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<AskFaqResponse>> askFaqQuestion(
            @Valid @RequestBody AskFaqRequest request) {
        
        AskFaqResponse responseData = faqService.askQuestion(request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Answer retrieved successfully", responseData)
        );
    }
}
