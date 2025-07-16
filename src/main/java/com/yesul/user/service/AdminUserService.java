package com.yesul.user.service;

import com.yesul.user.model.dto.response.UserListResponseDto;

import java.util.List;

public interface AdminUserService {

    List<UserListResponseDto> getAllUsersInfo();

    void updateUserStatus(Long userId, String displayStatus);

}
