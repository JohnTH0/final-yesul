package com.yesul.admin.repository;

import com.yesul.admin.model.dto.userInfo.UserInfoListAdminDto;
import com.yesul.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserInfoAdminRepository extends JpaRepository<User, Long> {
    @Query("SELECT new com.yesul.admin.model.dto.userInfo.UserInfoListAdminDto(u.id, " +
            "u.email, u.nickname, u.birthday, u.status ,u.createdAt) FROM User u")
    List<UserInfoListAdminDto> findAllUserInfoDto();

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    int updateUserStatus(Long userId, Character status);
}
