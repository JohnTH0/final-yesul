package com.yesul.community.repository;

import com.yesul.community.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findByBoardName(String boardName, Pageable pageable);
    List<Post> findByBoardName(String boardName); // 비페이징 용도
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Post> findByIdWithImages(@Param("id") Long id);
    @Query("SELECT p FROM Post p WHERE p.boardName = :boardName AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> searchByBoardNameAndKeyword(@Param("boardName") String boardName,
                                           @Param("keyword") String keyword,
                                           Pageable pageable);

}