package com.opom.bankingapp.controller.bankadmin;

import com.opom.bankingapp.dto.account.AccountAdminResponse;
import com.opom.bankingapp.dto.account.AccountListAdminResponse;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.features.account.service.AccountAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank-admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {

    private final AccountAdminService accountAdminService;

    public AccountController(AccountAdminService accountAdminService) {
        this.accountAdminService = accountAdminService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AccountListAdminResponse>> getAllAccounts() {
        AccountListAdminResponse responseData = accountAdminService.getAllAccounts();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Accounts retrieved successfully", responseData)
        );
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountAdminResponse>> getAccountById(@PathVariable Long accountId) {
        AccountAdminResponse responseData = accountAdminService.getAccountById(accountId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Account retrieved successfully", responseData)
        );
    }
}
