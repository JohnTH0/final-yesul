package com.yesul.admin.model.dto.alcohol;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlcoholListAdminDto {
    private Long id;
    private String brand;
    private String name;
    private String abv;
    private String region;
    private Boolean hasAttachment; // 첨부파일 여부
}
