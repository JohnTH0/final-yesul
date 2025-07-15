package com.yesul.community.repository;

import com.yesul.community.model.entity.Point;
import com.yesul.community.model.entity.enums.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByType(PointType type);
}