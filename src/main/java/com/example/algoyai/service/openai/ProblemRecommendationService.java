package com.example.algoyai.service.openai;


import com.example.algoyai.model.dto.openai.JsonRecommendationResponseDto;
import com.example.algoyai.model.dto.openai.ProblemRecommendationDto;
import com.example.algoyai.model.dto.openai.RecommendedProblemDto;
import com.example.algoyai.repository.solvedac.ProblemRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * /openai/json 추천 결과를 MongoDB에 저장하는 전용 서비스.
 */
@Service
@RequiredArgsConstructor
public class ProblemRecommendationService {

    private final ProblemRecommendationRepository problemRecommendationRepository;

    /**
     * 추천 결과를 problem_recommendations 컬렉션에 저장한다.
     *
     * @param userId Algoy 서비스 기준 사용자 아이디
     * @param recommendations OpenAI JSON 응답에서 파싱한 추천 결과 목록
     */
    public void saveRecommendation(String userId, List<JsonRecommendationResponseDto> recommendations) {
        List<RecommendedProblemDto> problems = recommendations.stream()
                .map(item -> RecommendedProblemDto.builder()
                        .problemNo(item.getProblemNo())
                        .title(item.getTitle())
                        .details(item.getDetails())
                        .build())
                .toList();

        ProblemRecommendationDto document = ProblemRecommendationDto.builder()
                .userId(userId)
                .recommendedProblems(problems)
                .build();

        problemRecommendationRepository.save(document);
    }
}
