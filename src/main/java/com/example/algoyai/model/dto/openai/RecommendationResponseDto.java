package com.example.algoyai.model.dto.openai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * OpenAI가 반환한 문제 추천 응답을 문제 1건 단위로 담는 DTO.
 *
 * 응답 문자열에서 사이트명, 문제명, 문제 번호, 추천 이유를 파싱해 저장하며,
 * 추천 결과 목록을 API 응답 형태로 전달할 때 사용된다.
 *
 * @author 조아라
 * @since 2026.04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDto {
    private String site;
    private String title;
    private String problemNo;
    private String details;
}
