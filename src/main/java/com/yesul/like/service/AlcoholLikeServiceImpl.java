package com.yesul.like.service;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.yesul.like.repository.AlcoholLikeRepository;
import com.yesul.alcohol.repository.AlcoholRepository;
import com.yesul.user.repository.UserRepository;
import com.yesul.alcohol.model.entity.Alcohol;
import com.yesul.user.model.entity.User;
import com.yesul.like.model.entity.AlcoholLike;
import com.yesul.like.model.dto.AlcoholLikeDto;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.exception.handler.AlcoholNotFoundException;

@Service
@RequiredArgsConstructor
public class AlcoholLikeServiceImpl implements AlcoholLikeService {

    private final AlcoholLikeRepository likeRepo;
    private final AlcoholRepository alcoholRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional
    public boolean toggleLike(Long alcoholId, Long userId) {
        // 좋아요가 이미 있는지 조회
        Optional<AlcoholLike> existing = likeRepo.findByAlcoholAndUser(alcoholId, userId);

        if (existing.isPresent()) {
            // 좋아요가 이미 있으면 삭제 후 false 반환 (좋아요 취소)
            likeRepo.deleteByAlcoholAndUser(alcoholId, userId);
            return false;
        }

        // 좋아요가 없으면 술과 사용자 객체를 찾아서 좋아요 생성 후 true 반환 (좋아요 등록)
        Alcohol alcohol = alcoholRepo.findById(alcoholId)
                .orElseThrow(() -> new AlcoholNotFoundException("술을 찾을 수 없습니다. ID=" + alcoholId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + userId));

        likeRepo.save(AlcoholLike.builder()
                .alcohol(alcohol)
                .user(user)
                .build()
        );

        return true;
    }

    @Override
    public int getLikeCount(Long alcoholId) {
        return likeRepo.countByAlcohol(alcoholId);
    }

    @Override
    public List<AlcoholLikeDto> getLikedAlcohols(Long userId) {
        return likeRepo.findLikesByUser(userId);
    }
}
