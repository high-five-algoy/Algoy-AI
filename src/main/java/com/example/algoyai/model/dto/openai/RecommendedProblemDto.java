package com.example.algoyai.model.dto.openai;


import lombok.*;

/**
 * 추천 문제 1개의 구조.
 *
 * problemNo:
 * - 백준 문제 번호
 *
 * title:
 * - 백준 문제 이름
 *
 * details:
 * - 해당 문제를 추천한 이유 또는 학습 포인트
 *
 * @author 조아라
 * @since 2026.04.19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedProblemDto {

    private String problemNo;

    private String title;

    private String details;
}
