package com.example.algoyai.model.dto.openai;


import lombok.*;

/**
 * /openai/json 응답용 DTO.
 * OpenAI JSON 응답과 MongoDB 저장 구조를 같은 의미로 맞춘다.
 *
 * @author 조아라
 * @since 2026.04.19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonRecommendationResponseDto {

    private String problemNo;

    private String title;

    private String details;
}
