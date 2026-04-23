package com.example.algoyai.model.dto.solvedac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * solved.ac 사용자 문제 이력 조회 API의 최상위 응답을 매핑하는 DTO.
 *
 * 전체 조회 건수(count)와 문제 목록(items)을 담으며,
 * items의 각 원소는 SolvedAcProblemDto로 매핑된다.
 * 이 응답 데이터는 사용자 풀이 이력을 기반으로
 * OpenAI 추천 프롬프트를 생성할 때 사용된다.
 *
 * @author 조아라
 * @since 2026.04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcTop100ResponseDto {

    private int count;
    private List<SolvedAcProblemDto> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SolvedAcProblemDto> getItems() {
        return items;
    }

    public void setItems(List<SolvedAcProblemDto> items) {
        this.items = items;
    }
}