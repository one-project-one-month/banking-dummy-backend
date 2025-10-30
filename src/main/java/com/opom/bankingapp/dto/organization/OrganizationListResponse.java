package com.opom.bankingapp.dto.organization;

import java.util.List;

public record OrganizationListResponse(
    List<OrganizationResponse> organizations
) {}
