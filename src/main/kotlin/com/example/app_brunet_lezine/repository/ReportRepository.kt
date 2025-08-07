package com.example.app_brunet_lezine.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.app_brunet_lezine.entity.Report
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, Long> {
    fun findByEvaluations_Id(evaluationId: Long): Report?
}
