package com.yesul.common.constants;

public class RedisConstants {

    public static final int TEMP_MESSAGE_DB_INDEX = 1; //채팅 시 임시 메시지 저장 index
    public static final int SYSTEM_MONITORING_DB_INDEX = 2; //시스템 모니터링 시 임시 메시지 저장 index
    public static final int USER_POINT_DB_INDEX = 3; // 유저 포인트 히스토리 저장 index

    private RedisConstants() {} //생성자 없을 시 디폴트 생성자를 만드나, 상수 클래스이므로 private을 설정(인스턴스화 하지 못하도록)
}
