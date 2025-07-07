package com.yesul.alcohol.repository;
import com.yesul.alcohol.model.dto.AlcoholSearchDto;
import com.yesul.alcohol.model.entity.Alcohol;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlcoholSpecification {

    public static Specification<Alcohol> searchWith(AlcoholSearchDto cond) {
        return new Specification<Alcohol>() {
            @Override
            public Predicate toPredicate(Root<Alcohol> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (cond.getProvince() != null)
                    predicates.add(cb.like(cb.lower(root.get("province")), "%" + cond.getProvince().toLowerCase() + "%"));
                if (cond.getCity() != null)
                    predicates.add(cb.like(cb.lower(root.get("city")), "%" + cond.getCity().toLowerCase() + "%"));
                if (cond.getName() != null)
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + cond.getName().toLowerCase() + "%"));
                if (cond.getBrand() != null)
                    predicates.add(cb.like(cb.lower(root.get("brand")), "%" + cond.getBrand().toLowerCase() + "%"));
                if (cond.getType() != null)
                    predicates.add(cb.equal(root.get("type"), cond.getType()));
                if (cond.getVolumeMl() != null)
                    predicates.add(cb.equal(root.get("volumeMl"), cond.getVolumeMl()));
                if (cond.getAbv() != null)
                    predicates.add(cb.equal(root.get("abv"), cond.getAbv()));
                if (cond.getIngredients() != null)
                    predicates.add(cb.like(cb.lower(root.get("ingredients")), "%" + cond.getIngredients().toLowerCase() + "%"));
                if (cond.getSweetnessLevel() != null)
                    predicates.add(cb.equal(root.get("sweetnessLevel"), cond.getSweetnessLevel()));
                if (cond.getAcidityLevel() != null)
                    predicates.add(cb.equal(root.get("acidityLevel"), cond.getAcidityLevel()));
                if (cond.getBodyLevel() != null)
                    predicates.add(cb.equal(root.get("bodyLevel"), cond.getBodyLevel()));
                if (cond.getAromaLevel() != null)
                    predicates.add(cb.equal(root.get("aromaLevel"), cond.getAromaLevel()));
                if (cond.getTanninLevel() != null)
                    predicates.add(cb.equal(root.get("tanninLevel"), cond.getTanninLevel()));
                if (cond.getFinishLevel() != null)
                    predicates.add(cb.equal(root.get("finishLevel"), cond.getFinishLevel()));
                if (cond.getSparklingLevel() != null)
                    predicates.add(cb.equal(root.get("sparklingLevel"), cond.getSparklingLevel()));
                if (cond.getPrice() != null)
                    predicates.add(cb.equal(root.get("price"), cond.getPrice()));
                if (cond.getPairingFoods() != null)
                    predicates.add(cb.like(cb.lower(root.get("pairingFoods")), "%" + cond.getPairingFoods().toLowerCase() + "%"));

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}