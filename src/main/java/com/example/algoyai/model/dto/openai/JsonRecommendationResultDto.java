package com.example.algoyai.model.dto.openai;


import lombok.*;

import java.util.List;

/**
 * OpenAI Structured Output 결과를 매핑하는 래퍼 DTO.
 *
 * @author 조아라
 * @since 2026.04.19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonRecommendationResultDto {

    private List<JsonRecommendationResponseDto> recommendedProblems;
}
