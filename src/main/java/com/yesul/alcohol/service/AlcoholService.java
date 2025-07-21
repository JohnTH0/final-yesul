package com.yesul.alcohol.service;

import com.yesul.alcohol.model.dto.AlcoholDetailDto;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.dto.AlcoholSearchDto;
import com.yesul.alcohol.model.entity.Alcohol;
import com.yesul.alcohol.repository.AlcoholRepository;
import com.yesul.alcohol.repository.AlcoholSpecification;
import com.yesul.exception.handler.RegistrationFailedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlcoholService {

    private final AlcoholRepository alcoholRepository;
    private final ModelMapper modelMapper;  // 생성자 주입

    public Page<AlcoholDetailDto> searchAlcohols(AlcoholSearchDto condition, Pageable pageable) {
        return alcoholRepository.findAll(AlcoholSpecification.searchWith(condition), pageable)
                .map(alcohol -> modelMapper.map(alcohol, AlcoholDetailDto.class));
    }

    public AlcoholDetailDto getAlcoholDetailById(Long id) {
        Alcohol alcohol = alcoholRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 술이 존재하지 않습니다."));

        return modelMapper.map(alcohol, AlcoholDetailDto.class);
    }

    public Map<String, Object> getAlcoholsAndPaging(int page) {
        Long number = 1L;
        alcoholRepository.findById(number);
        return Map.of();
    }

    public AlcoholDto getAlcohol(int alcoholId) {
        return null;
    }

    public Page<AlcoholDto> getAlcoholList(Pageable pageable) {
        return alcoholRepository.findAlcoholList(pageable);
    }

    public void registAlcohol(AlcoholDetailDto alcohol) {
        try{
            alcoholRepository.save(modelMapper.map(alcohol, Alcohol.class));
        } catch (Exception e) {
            throw new RegistrationFailedException("주류 등록에 실패했습니다.", e);
        }
    }

    public void updateAlcohol(AlcoholDetailDto dto) {
        alcoholRepository.save(modelMapper.map(dto, Alcohol.class));
    }

    public void deleteAlcoholById(Long id) {
        alcoholRepository.deleteById(id);
    }

}