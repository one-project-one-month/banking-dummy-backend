package com.opom.bankingapp.features.nickname.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameListResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameRequest;
import com.opom.bankingapp.features.nickname.service.NicknameService;
import com.opom.bankingapp.model.UserPrincipal;

@RestController
@RequestMapping("/personal-banking/users/nickname")
public class NicknameController {

    private final NicknameService nicknameService;

    public NicknameController(NicknameService nicknameService) {
        this.nicknameService = nicknameService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> setNickname(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody NicknameRequest request) {
        
        nicknameService.saveNickname(user.getId(), request);
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Nickname set successfully", null)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<NicknameListResponse>> getNicknames(
            @AuthenticationPrincipal UserPrincipal user) {
        
        NicknameListResponse response = nicknameService.getNicknamesByUserId(user.getId());
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Nicknames retrieved successfully", response)
        );
    }

    @PutMapping("/{nicknameId}")
    public ResponseEntity<ApiResponse<String>> updateNickname(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long nicknameId,
            @RequestBody NicknameRequest request) {
        
        nicknameService.updateNickname(user.getId(), nicknameId, request);
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Nickname updated successfully", null)
        );
    }

    @DeleteMapping("/{nicknameId}")
    public ResponseEntity<ApiResponse<String>> deleteNickname(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long nicknameId) {
        
        nicknameService.deleteNickname(user.getId(), nicknameId);
        
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Nickname deleted successfully", null)
        );
    }
}

