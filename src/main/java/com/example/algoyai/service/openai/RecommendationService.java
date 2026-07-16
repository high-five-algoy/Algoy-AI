package com.example.algoyai.service.openai;


import com.example.algoyai.model.dto.openai.RecommendationResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final OpenAiPromptService openAiPromptService;
    private final OpenAiService openAiService;

    /**
     * 문제 추천 받는 로직을 모두 순차적으로 호출
     *
     * @author ARa Cho
     * @return SolvedAcTop100Response
     * 컨트롤러에서 호출하는 메서드
     *
     */
    public String recommendProblems(String solvedAcUserName) {
        // SolvedAC API 호출 하여 사용자 문제 풀이 내역을 가져옴
        // 프롬프트 생성
        String prompt = openAiPromptService.buildPromptFromSolvedAc(solvedAcUserName);
        // OpenAI API 호출 하여 응답 결과 리턴
        JsonNode response = openAiService.callOpenAi(prompt);
        return openAiService.extractTextResponse(response);
    }

    public List<RecommendationResponseDto> recommendJsonProblems(String solvedAcUserName) {
        String prompt = openAiPromptService.buildPromptFromSolvedAc(solvedAcUserName);
        JsonNode response = openAiService.callOpenAi(prompt);
        String content = openAiService.extractTextResponse(response);
        return openAiService.parseRecommendationResponse(content);

    }
}
