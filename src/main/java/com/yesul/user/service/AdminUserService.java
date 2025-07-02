package com.yesul.user.service;

import com.yesul.user.model.dto.UserListDto;

import java.util.List;

public interface AdminUserService {

    List<UserListDto> getAllUsersInfo();

    void updateUserStatus(Long userId, String displayStatus);

}
