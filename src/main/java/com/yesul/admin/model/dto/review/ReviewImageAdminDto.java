package com.yesul.admin.model.dto.review;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewImageAdminDto { // 용도: 이상한 사진 삭제용 (대체사진도 엔티티 고려해주기)
    private Long id;
    private Long reviewId;
    private String imageUrl; // 이미지 여러개 조회 (ex. 처음 5개 조회 + 더보기)
}
