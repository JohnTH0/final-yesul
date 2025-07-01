package com.yesul.admin.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
    GENERAL('1', "일반"),
    UNVERIFIED('2', "미인증"),
    WITHDRAWN('3', "탈퇴"),
    SUSPENDED('4', "정지"),
    UNKNOWN('0', "알 수 없음");

    private final char status;
    private final String displayStatus;

    // 문자 → enum
    public static StatusType fromStatus(char status) {
        for (StatusType statusType : values()) {
            if (statusType.status == status) return statusType;
        }
        return UNKNOWN;
    }

    // 문자열 → enum
    public static StatusType fromDisplayStatus(String displayStatus) {
        for (StatusType statusType : values()) {
            if (statusType.displayStatus.equals(displayStatus)) return statusType;
        }
        return UNKNOWN;
    }

    // '1' → "인증"
    public static char displayStatusToStatus(String displayStatus) {
        return fromDisplayStatus(displayStatus).getStatus();
    }

    // "인증" → '1'
    public static String statusToDisplayStatus(char status) {
        return fromStatus(status).getDisplayStatus();
    }
}
