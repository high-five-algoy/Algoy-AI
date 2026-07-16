package com.example.algoyai.service.openai;


import com.example.algoyai.model.dto.solvedac.SolvedAcProblemDto;
import com.example.algoyai.model.dto.solvedac.SolvedAcTagDto;
import com.example.algoyai.model.dto.solvedac.SolvedAcTop100ResponseDto;
import com.example.algoyai.service.solvedac.SolvedAcService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * /openai/json 전용 프롬프트 생성 서비스.
 *
 * solvedacusername 으로 solved.ac 데이터를 조회한 뒤,
 * OpenAI가 구조화된 추천 결과를 만들 수 있도록 프롬프트를 생성한다.
 *
 * @Since 2026.04.19
 */
@Service
@RequiredArgsConstructor
public class OpenAiJsonPromptService {

    @Value("${openai.api.recommendation-count}")
    private int recommendationCount;

    private final SolvedAcService solvedAcService;

    /**
     * solved.ac 전적을 기반으로 /openai/json 전용 프롬프트를 생성한다.
     *
     * @param solvedAcUserName solved.ac 조회용 아이디
     * @return JSON 응답 생성용 프롬프트
     */
    public String buildJsonPromptFromSolvedAc(String solvedAcUserName) {
        SolvedAcTop100ResponseDto response = solvedAcService.getTop100(solvedAcUserName);

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return """
                    사용자의 solved.ac 풀이 이력이 없습니다.
                    백준 문제 %d개를 추천하세요.
                    각 문제는 반드시 problemNo, title, details 정보를 포함해야 합니다.
                    details에는 문제를 추천하는 이유를 구체적으로 작성하세요.
                    """.formatted(recommendationCount);
        }

        String solvedHistory = response.getItems().stream()
                .limit(30)
                .map(this::toProblemSummary)
                .collect(Collectors.joining("\n"));

        return """
                아래는 사용자의 solved.ac 상위 풀이 문제 목록입니다.

                %s

                위 풀이 기록을 바탕으로 사용자의 현재 실력과 풀이 패턴에 맞는 백준 문제 %d개를 추천하세요.

                각 추천 문제는 아래 의미를 따라 작성하세요.
                - problemNo: 백준 문제 번호만 작성하세요. 숫자 문자열이어야 합니다.
                - title: 백준 문제 제목만 작성하세요. 문제 번호를 넣으면 안 됩니다.
                - details: 이 사용자가 왜 이 문제를 풀어야 하는지에 대한 구체적인 추천 이유

                details은 짧은 키워드만 쓰지 말고, 사용자의 풀이 이력과 연결되는 설명으로 작성하세요.
                """.formatted(solvedHistory, recommendationCount);
    }

    /**
     * solved.ac 문제 1개를 프롬프트 입력용 문자열로 변환한다.
     */
    private String toProblemSummary(SolvedAcProblemDto problem) {
        String tags = extractTagKeys(problem.getTags());

        return String.format(
                "문제번호: %d, 제목: %s, 난이도(level): %d, 태그: %s",
                problem.getProblemId(),
                problem.getTitleKo(),
                problem.getLevel(),
                tags
        );
    }

    /**
     * 태그 목록을 프롬프트에 넣기 좋은 문자열로 변환한다.
     */
    private String extractTagKeys(List<SolvedAcTagDto> tags) {
        if (tags == null || tags.isEmpty()) {
            return "없음";
        }

        return tags.stream()
                .map(SolvedAcTagDto::getKey)
                .limit(3)
                .collect(Collectors.joining(", "));
    }
}
