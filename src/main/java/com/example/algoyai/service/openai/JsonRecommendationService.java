package com.example.algoyai.service.openai;


import com.example.algoyai.model.dto.openai.JsonRecommendationResponseDto;
import com.example.algoyai.model.dto.openai.JsonRecommendationResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * /openai/json 요청 전체 흐름을 담당하는 전용 서비스.
 *
 * 처리 순서:
 * 1. solved.ac 데이터 기반 프롬프트 생성
 * 2. OpenAI Structured Output 호출
 * 3. JSON 응답 DTO 변환
 * 4. MongoDB 저장
 * 5. API 응답 반환
 *
 * @Since 2026.04.19
 */
@Service
@RequiredArgsConstructor
public class JsonRecommendationService {

    private final OpenAiJsonPromptService openAiJsonPromptService;
    private final OpenAiJsonService openAiJsonService;
    private final ProblemRecommendationService problemRecommendationService;

    /**
     * JSON 추천 결과를 생성하고 저장한 뒤 반환한다.
     *
     * @param userId MongoDB에 저장할 사용자 아이디
     * @param solvedAcUserName solved.ac 조회용 아이디
     * @return 추천 문제 5개 목록
     */
    public List<JsonRecommendationResponseDto> recommendJsonProblems(String userId, String solvedAcUserName) {
        //solvedAC에서 사용자 정보 가져와서 프롬프트 작성하기
        String prompt = openAiJsonPromptService.buildJsonPromptFromSolvedAc(solvedAcUserName);

        //OpenAI API 호출하여 응답 데이터 받아온다
        //데이터를 JsonRecommendationResultDto에 맞게 변형한다
        JsonRecommendationResultDto result = openAiJsonService.callOpenAiForJson(prompt);

        //
        List<JsonRecommendationResponseDto> recommendations = result.getRecommendedProblems();

        //MongoDB에 저장
        problemRecommendationService.saveRecommendation(userId, recommendations);

        return recommendations;
    }
}
