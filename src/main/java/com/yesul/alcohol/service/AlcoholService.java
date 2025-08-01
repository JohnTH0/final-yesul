package com.yesul.alcohol.service;

import com.yesul.alcohol.model.dto.AlcoholDetailDto;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.dto.AlcoholSearchDto;
import com.yesul.alcohol.model.entity.Alcohol;
import com.yesul.alcohol.repository.AlcoholRepository;
import com.yesul.alcohol.repository.AlcoholSpecification;
import com.yesul.exception.handler.EntityNotFoundException;
import com.yesul.exception.handler.RegistrationFailedException;
import com.yesul.like.repository.AlcoholLikeRepository;
import com.yesul.exception.handler.RegistrationFailedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlcoholService {

    private final AlcoholRepository alcoholRepository;
    private final AlcoholLikeRepository alcoholLikeRepository;
    private final ModelMapper modelMapper;  // 생성자 주입

    public Page<AlcoholDetailDto> searchAlcohols(AlcoholSearchDto condition, Pageable pageable) {
        return alcoholRepository.findAll(AlcoholSpecification.searchWith(condition), pageable)
                .map(alcohol -> modelMapper.map(alcohol, AlcoholDetailDto.class));
    }

    public Page<AlcoholDetailDto> searchAlcoholsByUserId(AlcoholSearchDto condition, Pageable pageable, Long userId) {
        Page<Alcohol> alcohols = alcoholRepository.findAll(AlcoholSpecification.searchWith(condition), pageable);

        Set<Long> likedAlcoholIds = (userId != null)
                ? alcoholLikeRepository.findLikedAlcoholIdsByUserId(userId)
                : Collections.emptySet();

        return alcohols.map(alcohol -> {
            AlcoholDetailDto dto = modelMapper.map(alcohol, AlcoholDetailDto.class);
            dto.setLiked(likedAlcoholIds.contains(alcohol.getId()) ? 1L : 0L);
            return dto;
        });
    }

    public AlcoholDetailDto getAlcoholDetailById(Long id) {
        Alcohol alcohol = alcoholRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 술이 존재하지 않습니다."));

        return modelMapper.map(alcohol, AlcoholDetailDto.class);
    }

    public AlcoholDetailDto getAlcoholDetailWithLikeById(Long id, Long userId) {
        Alcohol alcohol = alcoholRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 술이 존재하지 않습니다."));

        AlcoholDetailDto dto = modelMapper.map(alcohol, AlcoholDetailDto.class);

        // userId가 null이 아닌 경우에만 좋아요 여부 확인
        if (userId != null) {
            boolean liked = alcoholLikeRepository.existsByUserIdAndAlcoholId(userId, id);
            dto.setLiked(liked ? 1L : 0L);
        } else {
            dto.setLiked(0L); // 로그인 안 한 경우 기본값
        }

        return dto;
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

    public void registAlcohol(AlcoholDetailDto dto) {
        try {
            Alcohol alcohol = Alcohol.builder()
                    .name(dto.getName())
                    .brand(dto.getBrand())
                    .abv(dto.getAbv())
                    .volumeMl(dto.getVolumeMl())
                    .province(dto.getProvince())
                    .city(dto.getCity())
                    .type(dto.getType())
                    .ingredients(dto.getIngredients())
                    .price(dto.getPrice())
                    .image(dto.getImage())
                    .description(dto.getDescription())
                    .tasteDescription(dto.getTasteDescription())
                    .pairingFoods(dto.getPairingFoods())
                    .sweetnessLevel(dto.getSweetnessLevel())
                    .acidityLevel(dto.getAcidityLevel())
                    .bodyLevel(dto.getBodyLevel())
                    .aromaLevel(dto.getAromaLevel())
                    .tanninLevel(dto.getTanninLevel())
                    .finishLevel(dto.getFinishLevel())
                    .sparklingLevel(dto.getSparklingLevel())
                    .build();

            alcoholRepository.save(alcohol);
        } catch (Exception e) {
            throw new RegistrationFailedException("주류 등록에 실패했습니다.", e);
        }
    }

    public void updateAlcohol(AlcoholDetailDto dto) {
        try {
            Alcohol alcohol = alcoholRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 주류를 찾을 수 없습니다."));

            alcohol.setName(dto.getName());
            alcohol.setBrand(dto.getBrand());
            alcohol.setAbv(dto.getAbv());
            alcohol.setVolumeMl(dto.getVolumeMl());
            alcohol.setProvince(dto.getProvince());
            alcohol.setCity(dto.getCity());
            alcohol.setType(dto.getType());
            alcohol.setIngredients(dto.getIngredients());
            alcohol.setPrice(dto.getPrice());
            alcohol.setImage(dto.getImage());
            alcohol.setDescription(dto.getDescription());
            alcohol.setTasteDescription(dto.getTasteDescription());
            alcohol.setPairingFoods(dto.getPairingFoods());
            alcohol.setSweetnessLevel(dto.getSweetnessLevel());
            alcohol.setAcidityLevel(dto.getAcidityLevel());
            alcohol.setBodyLevel(dto.getBodyLevel());
            alcohol.setAromaLevel(dto.getAromaLevel());
            alcohol.setTanninLevel(dto.getTanninLevel());
            alcohol.setFinishLevel(dto.getFinishLevel());
            alcohol.setSparklingLevel(dto.getSparklingLevel());

        } catch (Exception e) {
            throw new RegistrationFailedException("주류 수정에 실패했습니다.", e);
        }
    }

    public void deleteAlcoholById(Long id) {
        alcoholRepository.deleteById(id);
    }

}