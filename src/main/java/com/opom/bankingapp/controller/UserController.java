package com.opom.bankingapp.controller;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.user.*;
import com.opom.bankingapp.service.UserService;
import com.opom.bankingapp.model.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personal-banking/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/set-pin")
    public ResponseEntity<ApiResponse<String>> setPin(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody SetPinRequest request) {
        
        userService.setPin(user.getId(), request.pin());
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "PIN set successfully", null)
        );
    }

    @PostMapping("/agree-policy")
    public ResponseEntity<ApiResponse<String>> agreeToPolicy(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody AgreePolicyRequest request) {
        
        userService.agreeToPolicy(user.getId(), request.policyAgreement());
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Policy agreement successful", null)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserDetails(
            @AuthenticationPrincipal UserPrincipal user) {
        
        UserDetailsResponse userDetails = userService.getUserDetails(user);
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "User details retrieved", userDetails)
        );
    }

    @GetMapping("/from-accounts")
    public ResponseEntity<ApiResponse<FromAccountsResponse>> getFromAccounts(
            @AuthenticationPrincipal UserPrincipal user) {

        FromAccountsResponse responseData = userService.getFromAccounts(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "From accounts retrieved", responseData)
        );
    }

    @GetMapping("/recent-transfer-list")
    public ResponseEntity<ApiResponse<RecentTransferListResponse>> getRecentTransferList(
            @AuthenticationPrincipal UserPrincipal user) {

        RecentTransferListResponse responseData = userService.getRecentTransfers(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Recent transfers retrieved", responseData)
        );
    }

    @PutMapping("/autoSaveRecepit")
    public ResponseEntity<ApiResponse<String>> setAutoSaveReceipt(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam("flag") boolean flag) {

        userService.setAutoSaveReceipt(user.getId(), flag);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Preference updated", null)
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(user.getId(), request.oldPassword(), request.newPassword());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Password changed successfully", null)
        );
    }

    @PostMapping("/verify-pin")
    public ResponseEntity<ApiResponse<String>> verifyPin(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody VerifyPinRequest request) {

        userService.verifyPin(user.getId(), request.oldPin());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "PIN verified", null)
        );
    }

    @PostMapping("/switch-account")
    public ResponseEntity<ApiResponse<String>> switchAccount(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody SwitchAccountRequest request) {

        userService.switchAccount(user.getId(), request.accountId());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Account switched successfully", null)
        );
    }
}