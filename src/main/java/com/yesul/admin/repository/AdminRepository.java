package com.yesul.admin.repository;

import com.yesul.admin.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findById(Long id);

}
