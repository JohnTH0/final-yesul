package com.yesul.alcohol.repository;

import com.yesul.alcohol.model.entity.Alcohol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlcoholRepository extends JpaRepository<Alcohol, Long>, JpaSpecificationExecutor<Alcohol> {
}