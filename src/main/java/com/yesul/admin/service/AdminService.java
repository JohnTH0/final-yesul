package com.yesul.admin.service;

import com.yesul.admin.model.dto.userInfo.UserInfoListAdminDto;
import com.yesul.admin.repository.UserInfoAdminRepository;
import com.yesul.exception.handler.UpdateFailedException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yesul.admin.model.enums.StatusType.*;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final UserInfoAdminRepository userInfoAdminRepository;
    private final ModelMapper modelMapper;

    public List<UserInfoListAdminDto> getAllUsersInfo() {
        List<UserInfoListAdminDto> userList = userInfoAdminRepository.findAllUserInfoDto();

        for (UserInfoListAdminDto dto : userList) {

            String displayStatus = statusToDisplayStatus(dto.getStatus());
            dto.setDisplayStatus(displayStatus);
        }

        return userList;
    }

    @Transactional
    public void updateUserStatus(Long userId, String displayStatus) {
        char status = displayStatusToStatus(displayStatus);

        int updatedCount = userInfoAdminRepository.updateUserStatus(userId, status);

        if(updatedCount == 0) {
            throw new UpdateFailedException("회원상태변경에 실패하였습니다.");
        }
    }

}
