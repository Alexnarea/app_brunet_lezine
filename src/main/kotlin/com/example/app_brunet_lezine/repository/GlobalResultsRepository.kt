package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.GlobalResults
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface GlobalResultsRepository: JpaRepository<GlobalResults, Long> {
    fun existsByEvaluationId(evaluationId: Long): Boolean
    fun findByEvaluationId(evaluationId: Long): Optional<GlobalResults>


}