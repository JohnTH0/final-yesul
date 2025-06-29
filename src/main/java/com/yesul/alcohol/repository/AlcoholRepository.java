package com.yesul.alcohol.repository;

import com.yesul.alcohol.model.entity.Alcohol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AlcoholRepository extends JpaRepository<Alcohol, Long>  {
    Optional<Alcohol> findByName(String name);
}