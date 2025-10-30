package com.opom.bankingapp.dto.faq;

import lombok.Getter;

@Getter
public enum FaqStatus {
    DRAFT(0, "draft"),
    PUBLISHED(1, "published");

    private final int code;
    private final String value;

    FaqStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FaqStatus fromValue(String value) {
        for (FaqStatus status : FaqStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        return DRAFT;
    }
}
