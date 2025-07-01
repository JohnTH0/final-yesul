package com.yesul.admin.repository;

import com.yesul.admin.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAdminRepository extends JpaRepository<Admin, Long> {

    Admin findByLoginId(String loginId);
}
