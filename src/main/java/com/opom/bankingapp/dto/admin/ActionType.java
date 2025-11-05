package com.opom.bankingapp.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Possible actions for account status change")
public enum ActionType {
    APPROVE,
    REJECT
}
