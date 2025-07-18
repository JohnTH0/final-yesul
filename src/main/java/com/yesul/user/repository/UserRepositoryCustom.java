package com.yesul.user.repository;

import java.util.List;

import com.yesul.user.model.dto.response.UserListResponseDto;

public interface UserRepositoryCustom {
    List<UserListResponseDto> findAllUserInfoDto();
}
