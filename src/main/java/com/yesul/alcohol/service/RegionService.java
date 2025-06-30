package com.yesul.alcohol.service;

import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.entity.Alcohol;
import com.yesul.alcohol.model.entity.Region;
import com.yesul.alcohol.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public Page<AlcoholDto> getAlcoholsByRegion(
            String province, Pageable pageable) {

        Region region = regionRepository.findByProvince(province)
                .orElseThrow(() -> new NoSuchElementException("Region not found"));

        // AlcoholRegion 리스트 → AlcoholDto 리스트로 변환
        List<AlcoholDto> dtos = region.getAlcoholRegions().stream()
                .map(ar -> {
                    Alcohol a = ar.getAlcohol();
                    return new AlcoholDto(a.getId(), a.getName(), a.getBrand(), a.getAbv());
                })
                .collect(Collectors.toList());


        // 오프셋, 사이즈 계산
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtos.size());

        List<AlcoholDto> pageContent = start <= dtos.size() ? dtos.subList(start, end) : Collections.emptyList();

        // PageImpl을 사용해 page 객체로 래핑
        return new PageImpl<>(pageContent, pageable, dtos.size());
    }
}