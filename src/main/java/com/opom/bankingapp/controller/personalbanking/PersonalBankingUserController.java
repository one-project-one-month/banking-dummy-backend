package com.opom.bankingapp.controller.personalbanking;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.opom.bankingapp.dto.admin.DepositResponse;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.dto.user.AgreePolicyRequest;
import com.opom.bankingapp.dto.user.ChangePasswordRequest;
import com.opom.bankingapp.dto.user.FromAccountsResponse;
import com.opom.bankingapp.dto.user.RecentTransferListResponse;
import com.opom.bankingapp.dto.user.SetPinRequest;
import com.opom.bankingapp.dto.user.SwitchAccountRequest;
import com.opom.bankingapp.dto.user.UpdateProfileRequest;
import com.opom.bankingapp.dto.user.UserDetailsResponse;
import com.opom.bankingapp.dto.user.VerifyPinRequest;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/personal-banking/users")
public class PersonalBankingUserController {

    private final UserService userService;
    
    public PersonalBankingUserController(UserService userService) {
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
    
//    @PutMapping("/me")
//    public ResponseEntity<ApiResponse<String>> changeUserDetails(
//            @AuthenticationPrincipal UserPrincipal user,
//            @RequestBody ChangeUserDetailRequest request) {
//
//        userService.changeUserDetails(user.getId(),request);
//        return ResponseEntity.ok(
//            new ApiResponse<>(HttpStatus.OK.value(), "User details changed successfully", null)
//        );
//    }
    
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
    
    @GetMapping("/get-deposit-list")
    public ResponseEntity<ApiResponse<List<DepositResponse>>> getDepositList(
            @AuthenticationPrincipal UserPrincipal user) {

        List<DepositResponse> deposits = userService.getDepositList(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Deposit list retrieved", deposits)
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

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateUserDetails(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody UpdateProfileRequest request) {

        UserDetailsResponse updatedDetails = userService.updateProfileDetails(user, request);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "User details updated successfully", updatedDetails)
        );
    }

    @GetMapping("/transaction-history")
    public ResponseEntity<ApiResponse<RecentTransferListResponse>> getTransactionHistory(
            @AuthenticationPrincipal UserPrincipal user) {

        RecentTransferListResponse responseData = userService.getTransactionHistory(user.getId());

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Transaction history retrieved", responseData)
        );
    }
}