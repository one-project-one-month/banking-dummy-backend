package com.opom.bankingapp.features.organization.repository;

import com.opom.bankingapp.dto.organization.OrganizationRequest;
import com.opom.bankingapp.dto.organization.OrganizationResponse;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository {
    Long save(Long createdBy, OrganizationRequest request);
    void update(Long organizationId, Long updatedBy, OrganizationRequest request);
    void delete(Long organizationId);
    Optional<OrganizationResponse> findById(Long organizationId);
    List<OrganizationResponse> findAll();
}
