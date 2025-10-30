package com.opom.bankingapp.features.organization.service;

import com.opom.bankingapp.dto.organization.OrganizationListResponse;
import com.opom.bankingapp.dto.organization.OrganizationRequest;
import com.opom.bankingapp.dto.organization.OrganizationResponse;

public interface OrganizationService {
    OrganizationResponse createOrganization(Long createdBy, OrganizationRequest request);
    OrganizationResponse updateOrganization(Long organizationId, Long updatedBy, OrganizationRequest request);
    void deleteOrganization(Long organizationId);
    OrganizationResponse getOrganizationById(Long organizationId);
    OrganizationListResponse getAllOrganizations();
}
