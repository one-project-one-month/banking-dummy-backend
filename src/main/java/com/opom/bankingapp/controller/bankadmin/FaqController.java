package com.opom.bankingapp.controller.bankadmin;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.faq.FaqDetailResponse;
import com.opom.bankingapp.dto.faq.FaqListResponse;
import com.opom.bankingapp.dto.faq.FaqRequest;
import com.opom.bankingapp.features.faq.service.FaqService;
import com.opom.bankingapp.model.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-admin/faqs")
@PreAuthorize("hasRole('ADMIN')")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FaqListResponse>> getAllFaqs() {
        FaqListResponse responseData = faqService.getAllFaqs();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "FAQs retrieved successfully", responseData)
        );
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<ApiResponse<FaqDetailResponse>> getFaqById(@PathVariable Long faqId) {
        FaqDetailResponse responseData = faqService.getFaqById(faqId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "FAQ retrieved successfully", responseData)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FaqDetailResponse>> createFaq(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody FaqRequest request) {

        FaqDetailResponse responseData = faqService.createFaq(user.getId(), request);

        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.CREATED.value(), "FAQ created successfully", responseData),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{faqId}")
    public ResponseEntity<ApiResponse<FaqDetailResponse>> updateFaq(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long faqId,
            @Valid @RequestBody FaqRequest request) {

        FaqDetailResponse responseData = faqService.updateFaq(faqId, user.getId(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "FAQ updated successfully", responseData)
        );
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<ApiResponse<String>> deleteFaq(@PathVariable Long faqId) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "FAQ deleted successfully", null)
        );
    }
}
