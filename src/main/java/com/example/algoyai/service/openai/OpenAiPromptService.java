package com.example.algoyai.service.openai;

import org.springframework.beans.factory.annotation.Value;
import com.example.algoyai.model.dto.solvedac.SolvedAcProblemDto;
import com.example.algoyai.model.dto.solvedac.SolvedAcTagDto;
import com.example.algoyai.model.dto.solvedac.SolvedAcTop100ResponseDto;
import com.example.algoyai.service.solvedac.SolvedAcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiPromptService {

    @Value("${openai.api.recommendation-count}")
    private int recommendationCount;

    private final SolvedAcService solvedAcService;


    /**
     * 사용자의 solved.ac 데이터를 OpenAI에 보낼 프롬프트로 변환하는 메서드
     *
     * @author ARa Cho
     * @return String
     * SolvedAcService의 getTop100 메서드 호출하여 SolvedAC API 사용
     */
    public String buildPromptFromSolvedAc(String userName) {
        SolvedAcTop100ResponseDto response = solvedAcService.getTop100(userName);

        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return """
                    사용자의 solved.ac 풀이 이력이 없습니다.
                    입문자 기준으로 백준 문제 %d개를 추천해주세요.
                    각 추천에는 문제 번호, 문제 이름, 추천 이유를 포함해주세요.
                    """.formatted(recommendationCount);
        }

        String solvedHistory = response.getItems().stream()
                .limit(30)
                .map(this::toProblemSummary)
                .collect(Collectors.joining("\n"));

        return """
                다음은 사용자의 solved.ac 상위 풀이 문제 목록입니다.

                %s

                위 풀이 이력을 바탕으로 사용자의 수준과 자주 푼 유형을 추정해서
                백준 문제 %d개를 추천해주세요.

                응답 형식:
                - 문제 번호
                - 문제 이름
                - 추천 이유
                """.formatted(solvedHistory, recommendationCount);
    }

    /**
     * 문제 1 개를 String으로 변환
     *
     * @author ARa Cho
     * @return String
     *
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
     * tag(문제의 알고리즘)를 String으로 변환
     *
     * @author ARa Cho
     * @return String
     * 알고리즘은 최대 3개까지 명시
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
