package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Evaluations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EvaluationsRepository: JpaRepository<Evaluations, Long> {

    // Última evaluación por cada niño con una clasificación específica
    @Query("""
        SELECT e FROM Evaluations e 
        WHERE e.id IN (
            SELECT e2.id FROM Evaluations e2
            WHERE e2.applicationDate = (
                SELECT MAX(e3.applicationDate) 
                FROM Evaluations e3 
                WHERE e3.children.id = e2.children.id
            )
            AND e2.globalResults.classification = :classification
        )
    """)
    fun findLatestEvaluationsWithClassification(
        @Param("classification") classification: String
    ): List<Evaluations>

    // Última evaluación por cada niño evaluado por un evaluador específico con una clasificación específica
    @Query("""
        SELECT e FROM Evaluations e 
        WHERE e.id IN (
            SELECT e2.id FROM Evaluations e2
            WHERE e2.evaluator.id = :evaluatorId
            AND e2.applicationDate = (
                SELECT MAX(e3.applicationDate) 
                FROM Evaluations e3 
                WHERE e3.children.id = e2.children.id
                AND e3.evaluator.id = :evaluatorId
            )
            AND e2.globalResults.classification = :classification
        )
    """)

    fun findLatestEvaluationsByEvaluatorWithClassification(
        @Param("evaluatorId") evaluatorId: Long,
        @Param("classification") classification: String
    ): List<Evaluations>

    @Query("""
    SELECT e FROM Evaluations e
    JOIN FETCH e.children
    JOIN FETCH e.evaluator
    WHERE e.id = :id
""")
    fun findByIdWithChildAndEvaluator(@Param("id") id: Long): Evaluations


    fun findByEvaluatorId(evaluatorId: Long): List<Evaluations>
}
