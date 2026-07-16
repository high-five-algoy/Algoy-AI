package com.example.algoyai.model.dto.openai;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JSON 추천 결과 1회를 저장하는 MongoDB 문서.
 *
 * userId:
 * - Algoy 서비스 기준 사용자 식별값
 *
 * recommendedProblems:
 * - OpenAI가 JSON으로 반환한 추천 문제 5개 목록
 *
 * createdAt / updatedAt:
 * - 추천 이력 생성 및 수정 시각
 *
 * @author 조아라
 * @since 2026.04
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "problem_recommendations")
public class ProblemRecommendationDto {

    @Id
    private String id;

    private String userId;

    private List<RecommendedProblemDto> recommendedProblems;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
