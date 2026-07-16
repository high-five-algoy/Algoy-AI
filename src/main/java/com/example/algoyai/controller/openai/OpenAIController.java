package com.example.algoyai.controller.openai;



import com.example.algoyai.model.dto.openai.JsonRecommendationResponseDto;
import com.example.algoyai.service.openai.JsonRecommendationService;
import com.example.algoyai.service.openai.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/openai")
@RequiredArgsConstructor
@Slf4j
public class OpenAIController {

    private final RecommendationService recommendationService;
    private final JsonRecommendationService jsonRecommendationService;

    @GetMapping("/string")
    public ResponseEntity<String> recommend(
            @RequestParam String algoyusername, String solvedacusername
    ) {
        log.info("OpenAI API 호출 요청 들어옴. algoyusername={}, solvedacusername={}",
                algoyusername, solvedacusername);
        String result = recommendationService.recommendProblems(solvedacusername);
        return ResponseEntity.ok(result);
    }



    /**
     * JSON 추천 결과를 생성하고 저장한 뒤 반환한다.
     */
    @GetMapping("/json")
    public ResponseEntity<List<JsonRecommendationResponseDto>> recommendJson(
            @RequestParam String algoyusername,
            @RequestParam String solvedacusername
    ) {
        List<JsonRecommendationResponseDto> result =
                jsonRecommendationService.recommendJsonProblems(algoyusername, solvedacusername);

        return ResponseEntity.ok(result);
    }

//    @GetMapping("/json")
//    public  ResponseEntity<List<RecommendationResponseDto>> recommendJson(
//            @RequestParam String algoyusername, String solvedacusername){
//        List<RecommendationResponseDto> result =
//                recommendationService.recommendJsonProblems(solvedacusername);
//
//        return ResponseEntity.ok(result);
//
//    }
}