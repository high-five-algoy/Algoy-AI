package com.example.algoyai.model.dto.allenai;

import com.google.gson.annotations.SerializedName;
import java.util.List;


/**
 * solved.ac 문제 조회 API의 JSON 응답을 매핑하는 DTO.
 *
 * 기존 Allen 추천 로직에서 solved.ac 응답의 count, items,
 * problemId, titleKo, titles 정보를 파싱하기 위해 사용된다.
 *
 * 주로 문제 번호와 제목 정보를 추출해 추천 요청용 문자열을 만드는 데 사용된다.
 *
 * @author 조아라
 * @since 2026.04
 */

//데이터베이스와 상관없는 Json 파싱을 위한 클래스 구현입니다
//JSON 구조를 반영한 Java 클래스 생성
//items -> titles -> title 최종 추출하여 리스트 생성
public class SolvedACResponseDto {
    //문제수(전체 원소 수)
    @SerializedName("count")
    private int count;
    //문제 내용(현재 페이지의 원소 목록)
    @SerializedName("items")
    private List<Item> items;

    public int getCount() {
        return count;
    }

    public List<Item> getItems() {
        return items;
    }

    // Item 문제 내용안에 또다른 Json
    public static class Item {
        //문제 아이디(백준 문제 번호로, 문제마다 고유)
        @SerializedName("problemId")
        private String problemId;
        //한국어 문제 제목입니다. HTML 엔티티나 LaTeX 수식을 포함할 수 있습니다.
        @SerializedName("titleKo")
        private String titleKo;
        //언어별 문제 제목 목록입니다.
        @SerializedName("titles")
        private List<Title> titles;

        public String getProblemId() { return problemId; }
        public String getTitleKo() {
            return titleKo;
        }

        public List<Title> getTitles() {
            return titles;
        }
    }
    //Titles 안에 또다른 Json
    public static class Title {
        //문제 제목이 작성된 언어입니다.
        @SerializedName("language")
        private String language;
        //문제 제목입니다. (***추출해야할 부분***)
        @SerializedName("title")
        private String title;

        public String getTitle() {
            return title;
        }

        public String getLanguage() {
            return language;
        }
    }
}

