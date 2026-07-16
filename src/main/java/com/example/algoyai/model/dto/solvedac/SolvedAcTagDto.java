package com.example.algoyai.model.dto.solvedac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * solved.ac API가 문제마다 내려주는 tags 배열의 각 원소를 받는 중첩 DTO
 * SolvedAcProblemDto 안에 List<SolvedAcTagDto> tags로 들어감
 * OpenAI 프롬프트 만들 때 문제 태그를 문자열로 붙이기 위해서 만듬
 *
 * @author 조아라
 * @since 2026.04
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedAcTagDto {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}