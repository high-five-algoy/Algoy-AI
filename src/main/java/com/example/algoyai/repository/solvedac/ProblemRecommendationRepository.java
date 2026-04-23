package com.example.algoyai.repository.solvedac;

import com.example.algoyai.model.dto.openai.ProblemRecommendationDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * problem_recommendations 컬렉션 접근용 Repository.
 *
 * @Since 2026.04.19
 */

public interface ProblemRecommendationRepository extends MongoRepository<ProblemRecommendationDto, String> {

    /**
     * 사용자 전체 추천 이력을 최신순으로 조회한다.
     */
    List<ProblemRecommendationDto> findByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * 사용자의 최신 추천 이력 1건만 조회한다.
     */
    Optional<ProblemRecommendationDto> findFirstByUserIdOrderByCreatedAtDesc(String userId);

    /**
     * 사용자의 특정 기간 추천 이력을 최신순으로 조회한다.
     */
    List<ProblemRecommendationDto> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String userId,
            LocalDateTime start,
            LocalDateTime end
    );
}
