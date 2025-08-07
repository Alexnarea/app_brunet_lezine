package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Recommendation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecommendationRepository: JpaRepository<Recommendation, Long> {
    fun findByGlobalResultId(resultId: Long): List<Recommendation>
}