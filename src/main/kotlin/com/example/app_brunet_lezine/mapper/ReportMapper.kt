package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.ReportDto
import com.example.app_brunet_lezine.entity.Evaluations
import com.example.app_brunet_lezine.entity.Report
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
object ReportMapper {

    fun toEntity(dto: ReportDto): Report {
        val report = Report()
        report.id = dto.id
        report.filePath = dto.filePath
        report.generatedAt = dto.generatedAt

        val evaluation = Evaluations()
        evaluation.id = dto.evaluationId
        report.evaluations = evaluation

        return report
    }

    fun toReportDto(entity: Report): ReportDto {
        val eval = entity.evaluations
            ?: throw IllegalStateException("Evaluation is null")

        val child = eval.children
        val evaluator = eval.evaluator
        val results = eval.globalResults

        return ReportDto(
            id = entity.id,
            evaluationId = eval.id!!,
            filePath = entity.filePath ?: "",
            generatedAt = entity.generatedAt,

            // Niño
            childFullName = child?.fullName ?: "N/A",
            childNui = child?.nui,
            childBirthdate = child?.birthdate?.toString(),
            childGender = child?.gender,

            // Evaluación
            applicationDate = eval.applicationDate ?: LocalDateTime.now(),
            chronologicalAgeMonths = eval.chronologicalAgeMonths ?: 0,

            // Resultados
            resultYears = results?.resultYears,
            resultDetail = results?.resultDetail,
            coefficient = results?.coefficient?.toDouble(),
            classification = results?.classification,
            totalMonthsApproved = results?.totalMonthsApproved?.toDouble(),

            // Ítems: si los necesitas aquí, deberás cargarlos desde ResponsesRepository en el `ReportService`
            items = emptyList() // se asignan por separado en el service (como ya lo haces)
        )
    }
}

