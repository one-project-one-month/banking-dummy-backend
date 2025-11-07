package com.opom.bankingapp.controller.bankadmin;

import com.opom.bankingapp.dto.admin.AccountActionRequest;
import com.opom.bankingapp.dto.admin.UserListAdminResponse;
import com.opom.bankingapp.dto.common.ApiResponse;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final AdminUserService adminUserService;

    public UserAdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Operation(
            summary = "Approve or Reject a user's pending registration",
            description = "Sets the user status to ACTIVE (APPROVE) or BLOCKED (REJECT).",
            requestBody = @RequestBody(
                    description = "The action to perform on the user's pending account",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountActionRequest.class),
                            examples = {
                                    @ExampleObject(name = "Approve Example", value = "{\"action\": \"APPROVE\"}",
                                            summary = "Activates the user account"),
                                    @ExampleObject(name = "Reject Example", value = "{\"action\": \"REJECT\"}",
                                            summary = "Blocks the user account")
                            }
                    )
            )
    )
    @PostMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<String>> processAccountAction(
            @AuthenticationPrincipal UserPrincipal adminUser,
            @PathVariable Long userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody AccountActionRequest request) {

        adminUserService.processAccountAction(adminUser.getId(), userId, request);

        String action = request.action().name().toLowerCase();

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "User account " + action + " successfully", null)
        );
    }

    @Operation(summary = "Get all users for admin panel")
    @GetMapping
    public ResponseEntity<ApiResponse<UserListAdminResponse>> getAllUsers() {
        UserListAdminResponse responseData = adminUserService.getAllUsers();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Users retrieved successfully", responseData)
        );
    }
}
