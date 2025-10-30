package com.opom.bankingapp.model;

import lombok.Getter;

@Getter
public enum UserStatus {
    PENDING(1, "Pending Verification"),
    ACTIVE(2, "Active"),
    INACTIVE(3, "Inactive/Suspended"),
    BLOCKED(4, "Blocked");

    private final int code;
    private final String value;

    UserStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static UserStatus fromCode(int code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus code: " + code);
    }
}
