package com.yesul.login.repository;

import com.yesul.admin.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminLoginRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByLoginId(String loginId);

}
