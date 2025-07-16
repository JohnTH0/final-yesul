package com.yesul.user.service;

import com.yesul.user.model.dto.response.UserListResponseDto;
import com.yesul.exception.handler.UpdateFailedException;
import com.yesul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yesul.admin.model.enums.StatusType.*;

@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    public List<UserListResponseDto> getAllUsersInfo() {
        List<UserListResponseDto> userList = userRepository.findAllUserInfoDto();

        for (UserListResponseDto dto : userList) {

            String displayStatus = statusToDisplayStatus(dto.getStatus());
            dto.setDisplayStatus(displayStatus);
        }

        return userList;
    }

    @Transactional
    public void updateUserStatus(Long userId, String displayStatus) {

        char status = displayStatusToStatus(displayStatus);

        int updatedCount = userRepository.updateUserStatus(userId, status);

        if(updatedCount == 0) {
            throw new UpdateFailedException("회원상태변경에 실패하였습니다.");
        }
    }
}
