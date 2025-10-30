package com.opom.bankingapp.features.organization.service.impl;

import com.opom.bankingapp.dto.organization.OrganizationListResponse;
import com.opom.bankingapp.dto.organization.OrganizationRequest;
import com.opom.bankingapp.dto.organization.OrganizationResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.features.organization.repository.OrganizationRepository;
import com.opom.bankingapp.features.organization.service.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    @Transactional
    public OrganizationResponse createOrganization(Long createdBy, OrganizationRequest request) {
        Long newId = organizationRepository.save(createdBy, request);

        return organizationRepository.findById(newId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created Organization."));
    }

    @Override
    @Transactional
    public OrganizationResponse updateOrganization(Long organizationId, Long updatedBy, OrganizationRequest request) {
        // Repository's update method handles existence check and throws ResourceNotFoundException
        organizationRepository.update(organizationId, updatedBy, request);

        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated Organization."));
    }

    @Override
    @Transactional
    public void deleteOrganization(Long organizationId) {
        organizationRepository.delete(organizationId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(Long organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with ID: " + organizationId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationListResponse getAllOrganizations() {
        return new OrganizationListResponse(organizationRepository.findAll());
    }
}
