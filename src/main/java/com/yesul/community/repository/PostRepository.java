package com.yesul.community.repository;

import com.yesul.community.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoardNameOrderByCreatedAtDesc(String boardName);
}