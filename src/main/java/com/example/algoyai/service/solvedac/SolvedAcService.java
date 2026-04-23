package com.example.algoyai.service.solvedac;


import com.example.algoyai.model.dto.solvedac.SolvedAcTop100ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;


@RequiredArgsConstructor
@Service
@Slf4j
public class SolvedAcService {

//    @Value("${solvedac.url}")
//    String solvedAcApi;

    @Value("${solvedac.base-url}")
    private String baseUrl;

    @Value("${solvedac.top100-path}")
    private String top100Path;

    private final WebClient.Builder webClientBuilder;


    /**
     * solvedAC API를 호출하여 사용자가 풀었던 문제 가져옴
     *
     * @author ARa Cho
     * @return SolvedAcTop100Response
     * WebFlux 의 Client를 사용
     */
    public SolvedAcTop100ResponseDto getTop100(String userName) {
        try {
            SolvedAcTop100ResponseDto response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(top100Path)
                            .queryParam("handle", userName)
                            .build())
                    .header("x-solvedac-language", "ko")
                    .retrieve()
                    .bodyToMono(SolvedAcTop100ResponseDto.class)
                    .block();

            log.info("Solved.ac response received. userName={}, itemCount={}",
                    userName,
                    response != null && response.getItems() != null ? response.getItems().size() : 0);

            return response;
        } catch (Exception e) {
            log.error("Solved.ac API call failed. userName={}", userName, e);
            throw e;
        }

//        return webClientBuilder
//                .baseUrl(baseUrl)
//                .build()
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path(top100Path)
//                        .queryParam("handle", userName)
//                        .build())
//                .header("x-solvedac-language", "ko")
//                .retrieve()
//                .bodyToMono(SolvedAcTop100Response.class)
//                .block();
    }
}