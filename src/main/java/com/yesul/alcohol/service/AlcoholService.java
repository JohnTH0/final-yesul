package com.yesul.alcohol.service;

import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.repository.AlcoholRepository;
import com.yesul.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlcoholService {

    private final AlcoholRepository alcoholRepository;

    public Map<String, Object> getAlcoholsAndPaging(int page) {
        Long number = 1L;
        alcoholRepository.findById(number);
        return Map.of();
    }

    public AlcoholDto getAlcohol(int alcoholId) {
        return null;
    }

    public Map<String, Object> registAlcohol(AlcoholDto alcohol) {
        return Map.of();
    }
}