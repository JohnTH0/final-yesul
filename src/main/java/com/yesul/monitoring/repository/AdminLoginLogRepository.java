package com.yesul.monitoring.repository;

import com.yesul.login.model.entity.AdminLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLoginLogRepository extends JpaRepository<AdminLoginLog, Long> {
}