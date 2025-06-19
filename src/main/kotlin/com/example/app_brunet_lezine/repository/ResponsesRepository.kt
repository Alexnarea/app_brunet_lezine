package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Responses
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResponsesRepository: JpaRepository<Responses, Long> {
    fun findByEvaluationId(evaluationId: Long): List<Responses>

}