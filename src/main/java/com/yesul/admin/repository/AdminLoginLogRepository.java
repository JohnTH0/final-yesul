package com.yesul.admin.repository;

import com.yesul.admin.model.entity.AdminLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminLoginLogRepository extends JpaRepository<AdminLoginLog, Long> {
    List<AdminLoginLog> findAllByOrderByCreatedAtDesc();
}