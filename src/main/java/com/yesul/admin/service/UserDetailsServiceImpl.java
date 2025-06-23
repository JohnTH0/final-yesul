package com.yesul.admin.service;

import com.yesul.admin.model.dto.AdminDto;
import com.yesul.admin.model.dto.auth.LoginAdminDto;
import com.yesul.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // private final AdminRepository AdminRepository;

    /**
     * 로그인(인증) 처리시 자동으로 실행되는 메소드
     *
     * @param userId - 폼 로그인 요청시 전달되는 관리자 아이디
     * @return         - 조회된 사용자의 정보 중 인증에 필요한 최소한의 정보가 담겨있는 AdminDetails객체 반환
     * @throws UsernameNotFoundException - 존재하는 계정이 없을 경우 발생시킬 예외
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        AdminDto fetchedAdmin = AdminDto.builder()
                .id(1L)
                .loginId("admin1")
                .loginPwd(passwordEncoder.encode("1234")) // 원본 비번은 1234
                .build(); // = userMapper.selectUserById(userId);  // 조회된 데이터

        if(fetchedAdmin == null){
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 잘못되었습니다."); // 표준예외클래스
        }

        List<GrantedAuthority> authorityList =
                List.of(new SimpleGrantedAuthority("ADMIN"));

        return new LoginAdminDto(fetchedAdmin.getId(), fetchedAdmin.getLoginId(), fetchedAdmin.getLoginPwd(), authorityList); // 이제 이걸로 security에서 인증처리 진행함.
    }

}