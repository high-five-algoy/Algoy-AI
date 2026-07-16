package com.example.algoyai.service.openai;


import com.example.algoyai.model.dto.openai.JsonRecommendationResultDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * /openai/json 전용 OpenAI 호출 서비스.
 *
 * Structured Outputs(JSON Schema)를 사용해
 * OpenAI가 처음부터 JSON 형식으로 응답하도록 요청한다.
 *
 * @Since 2026.04.19
 */

@Service
@RequiredArgsConstructor
public class OpenAiJsonService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.url}")
    private String openAiUrl;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.recommendation-count}")
    private int recommendationCount;

    /**
     * JSON schema 기반으로 OpenAI를 호출하고 추천 결과를 DTO로 변환한다.
     *
     * @param prompt /openai/json 전용 프롬프트
     * @return JSON 구조로 생성된 추천 결과
     */
    public JsonRecommendationResultDto callOpenAiForJson(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt,
                "text", Map.of(
                        "format", Map.of(
                                "type", "json_schema",
                                "name", "problem_recommendation_response",
                                "strict", true,
                                "schema", Map.of(
                                        "type", "object",
                                        "properties", Map.of(
                                                "recommendedProblems", Map.of(
                                                        "type", "array",
                                                        "minItems", recommendationCount,
                                                        "maxItems", recommendationCount,
                                                        "items", Map.of(
                                                                "type", "object",
                                                                "properties", Map.of(
                                                                        "problemNo", Map.of("type", "string"),
                                                                        "title", Map.of("type", "string"),
                                                                        "details", Map.of("type", "string")
                                                                ),
                                                                "required", List.of("problemNo", "title", "details"),
                                                                "additionalProperties", false
                                                        )
                                                )
                                        ),
                                        "required", List.of("recommendedProblems"),
                                        "additionalProperties", false
                                )
                        )
                )
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
                                        "OpenAI JSON API 호출 실패: status=%s body=%s"
                                                .formatted(clientResponse.statusCode(), body)
                                ))
                )
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("OpenAI JSON 응답 본문이 비어 있습니다.");
        }

        String jsonText = extractJsonText(response);

        try {
            return objectMapper.readValue(jsonText, JsonRecommendationResultDto.class);
        } catch (Exception e) {
            throw new IllegalStateException("OpenAI JSON 응답 파싱에 실패했습니다. body=" + jsonText, e);
        }
    }

    /**
     * Responses API 응답의 output[].content[].text 에서 JSON 문자열 본문을 추출한다.
     */
    private String extractJsonText(JsonNode response) {
        JsonNode output = response.path("output");

        if (!output.isArray() || output.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답에 output 이 없습니다: " + response);
        }

        StringBuilder result = new StringBuilder();

        for (JsonNode item : output) {
            JsonNode content = item.path("content");
            if (!content.isArray()) {
                continue;
            }

            for (JsonNode c : content) {
                String text = c.path("text").asText(null);
                if (text != null && !text.isBlank()) {
                    result.append(text);
                }
            }
        }

        if (result.isEmpty()) {
            throw new IllegalStateException("OpenAI 응답에서 JSON text 를 추출하지 못했습니다: " + response);
        }

        return result.toString();
    }
}
