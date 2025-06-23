package com.yesul.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yesul.user.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByMail(String mail);
}