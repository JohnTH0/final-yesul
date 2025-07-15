package com.yesul.community.model.entity.enums;

/**
 * 포인트 활동 유형 Enum
 * - DB에는 문자열로 저장: "post_create", "comment_create", "attendance"
 */
public enum PointType {
    POST_CREATE("post_create"),
    COMMENT_CREATE("comment_create"),
    ATTENDANCE("attendance");

    private final String type;

    PointType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 문자열 → Enum 변환
     * 예외처리도 포함 (알 수 없는 값은 예외 던짐)
     */
    public static PointType from(String type) {
        for (PointType pt : PointType.values()) {
            if (pt.getType().equalsIgnoreCase(type)) {
                return pt;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 포인트 타입입니다: " + type);
    }
}