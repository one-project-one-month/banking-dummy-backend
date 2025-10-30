package com.opom.bankingapp.controller.bankadmin;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.organization.OrganizationListResponse;
import com.opom.bankingapp.dto.organization.OrganizationRequest;
import com.opom.bankingapp.dto.organization.OrganizationResponse;
import com.opom.bankingapp.features.organization.service.OrganizationService;
import com.opom.bankingapp.model.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-admin/organizations")
@PreAuthorize("hasRole('ADMIN')")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrganizationListResponse>> getAllOrganizations() {
        OrganizationListResponse responseData = organizationService.getAllOrganizations();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Organizations retrieved successfully", responseData)
        );
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(@PathVariable Long organizationId) {
        OrganizationResponse responseData = organizationService.getOrganizationById(organizationId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Organization retrieved successfully", responseData)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody OrganizationRequest request) {

        OrganizationResponse responseData = organizationService.createOrganization(user.getId(), request);

        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.CREATED.value(), "Organization created successfully", responseData),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long organizationId,
            @Valid @RequestBody OrganizationRequest request) {

        OrganizationResponse responseData = organizationService.updateOrganization(organizationId, user.getId(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Organization updated successfully", responseData)
        );
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<ApiResponse<String>> deleteOrganization(@PathVariable Long organizationId) {
        organizationService.deleteOrganization(organizationId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Organization deleted successfully", null)
        );
    }
}
