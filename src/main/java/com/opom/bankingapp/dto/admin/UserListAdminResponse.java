package com.opom.bankingapp.dto.admin;

import java.util.List;

public record UserListAdminResponse(
    List<UserAdminResponse> users
) {}
