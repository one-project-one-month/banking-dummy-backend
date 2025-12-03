package com.opom.bankingapp.controller.bankadmin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opom.bankingapp.dto.admin.DepositRequest;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.AdminUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bank-admin/deposit")
@PreAuthorize("hasRole('ADMIN')")
public class DepositController {
	
	private final AdminUserService adminUserService;
	public DepositController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createDeposit(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody DepositRequest request) {

        adminUserService.createDeposit(user.getId(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Deposit success.", null)
        );
    }
}
