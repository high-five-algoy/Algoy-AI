package com.example.algoyai.service.openai;

import com.example.algoyai.model.dto.openai.RecommendationResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api.url}")
    private String openAiUrl;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.api.model}")
    private String model;




    /**
     * 프롬프트를 OpenAI에 보내고, 추천 결과를 받아오는 메서드
     *
     * @author ARa Cho
     * @return String
     *
     */

    public JsonNode callOpenAi(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt
        );

        JsonNode response = webClientBuilder.build()
                .post()
                .uri(openAiUrl)
                .header("Authorization", "Bearer " + openAiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> new IllegalStateException(
                                        "OpenAI API 호출 실패: status=%s body=%s"
                                                .formatted(clientResponse.statusCode(), body)
                                ))
                )
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("OpenAI 응답 본문이 비어 있습니다.");
        }

        JsonNode output = response.path("output");
        if (!output.isArray() || output.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답에 output이 없습니다: " + response);
        }

        return response;
    }

    public String extractTextResponse(JsonNode response) {
        JsonNode output = response.path("output");
        StringBuilder result = new StringBuilder();

        for (JsonNode item : output) {
            JsonNode content = item.path("content");
            if (!content.isArray()) {
                continue;
            }

            for (JsonNode c : content) {
                String text = c.path("text").asText(null);
                if (text != null && !text.isBlank()) {
                    if (!result.isEmpty()) {
                        result.append('\n');
                    }
                    result.append(text);
                }
            }
        }

        if (result.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답에서 텍스트를 추출하지 못했습니다: " + response);
        }

        return result.toString();
    }

    public List<RecommendationResponseDto> parseRecommendationResponse(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("OpenAI 추천 응답이 비어 있습니다.");
        }

        List<RecommendationResponseDto> result = new ArrayList<>();

        String[] blocks = content.trim().split("\\r?\\n\\s*\\r?\\n");

        for (String block : blocks) {
            String site = "";
            String title = "";
            String problemNo = "";
            String details = "";

            String[] lines = block.split("\\R");

            for (String rawLine : lines) {
                String line = rawLine.trim();

                if (line.matches("^\\d+\\..*")) {
                    line = line.replaceFirst("^\\d+\\.\\s*", "");
                }

                if (line.startsWith("사이트:")) {
                    site = line.substring("사이트:".length()).trim();
                } else if (line.startsWith("제목:")) {
                    title = line.substring("제목:".length()).trim();
                } else if (line.startsWith("문제번호:")) {
                    problemNo = line.substring("문제번호:".length()).trim();
                } else if (line.startsWith("설명:")) {
                    details = line.substring("설명:".length()).trim();
                }
            }

            if (!site.isBlank() || !title.isBlank() || !problemNo.isBlank() || !details.isBlank()) {
                result.add(RecommendationResponseDto.builder()
                        .site(site)
                        .title(title)
                        .problemNo(problemNo)
                        .details(details)
                        .build());
            }
        }

        if (result.isEmpty()) {
            throw new IllegalStateException("RecommendationResponseDto 목록으로 파싱하지 못했습니다.");
        }

        return result;
    }



//    public String requestRecommendation(String prompt) {
//        //OpenAI API에 보낼 JSON
//        Map<String, Object> requestBody = Map.of(
//                "model", model,
//                "input", prompt
//        );
//
//        //OpneAI API 호출
//        JsonNode response = webClientBuilder.build()
//                .post()
//                .uri(openAiUrl)
//                .header("Authorization", "Bearer " + openAiKey)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(requestBody)
//                .retrieve()
//                .onStatus(
//                        HttpStatusCode::isError,
//                        clientResponse -> clientResponse.bodyToMono(String.class)
//                                .defaultIfEmpty("")
//                                .map(body -> new IllegalStateException(
//                                        "OpenAI API 호출 실패: status=%s body=%s"
//                                                .formatted(clientResponse.statusCode(), body)
//                                ))
//                )
//                .bodyToMono(JsonNode.class)
//                .block();
//
//
//        if (response == null) {
//            throw new IllegalStateException("OpenAI 응답이 비어 있습니다.");
//        }
//
//        JsonNode output = response.path("output");
//        if (!output.isArray() || output.isEmpty()) {
//            throw new IllegalStateException("OpenAI 응답에 output이 없습니다: " + response);
//        }
//
//        StringBuilder result = new StringBuilder();
//
//        for (JsonNode item : output) {
//            JsonNode content = item.path("content");
//            if (!content.isArray()) {
//                continue;
//            }
//
//            for (JsonNode c : content) {
//                String text = c.path("text").asText(null);
//                if (text != null && !text.isBlank()) {
//                    if (!result.isEmpty()) {
//                        result.append('\n');
//                    }
//                    result.append(text);
//                }
//            }
//        }
//
//        if (result.isEmpty()) {
//            throw new IllegalStateException("OpenAI 응답에서 텍스트를 추출하지 못했습니다: " + response);
//        }
//
//        return result.toString();
//
//    }



}
