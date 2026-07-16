package com.example.algoyai.model.dto.solvedac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * solved.ac 문제 조회 응답에서 문제 1건을 매핑하는 DTO.
 *
 * SolvedAcTop100ResponseDto의 items 목록에 포함되며,
 * 문제 번호(problemId), 한글 제목(titleKo), 난이도(level), 태그(tags) 정보를 담는다.
 * 이 데이터는 사용자 풀이 이력을 기반으로 OpenAI 추천 프롬프트를 생성할 때 사용
 *
 * @author 조아라
 * @since 2026.04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcProblemDto {

    private int problemId;
    private String titleKo;
    private int level;
    private List<SolvedAcTagDto> tags;

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getTitleKo() {
        return titleKo;
    }

    public void setTitleKo(String titleKo) {
        this.titleKo = titleKo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<SolvedAcTagDto> getTags() {
        return tags;
    }

    public void setTags(List<SolvedAcTagDto> tags) {
        this.tags = tags;
    }
}