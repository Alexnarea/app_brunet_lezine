package com.example.app_brunet_lezine.service

import com.example.app_brunet_lezine.dto.ReportDto
import com.example.app_brunet_lezine.entity.Report
import com.example.app_brunet_lezine.mapper.ReportMapper
import com.example.app_brunet_lezine.repository.EvaluationsRepository
import com.example.app_brunet_lezine.repository.ReportRepository
import com.example.app_brunet_lezine.repository.ResponsesRepository
import com.example.app_brunet_lezine.util.ReportGenerator
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class ReportService {

    @Autowired
    private lateinit var globalResultsService: GlobalResultsService

    @Autowired
    lateinit var reportRepository: ReportRepository

    @Autowired
    lateinit var evaluationsRepository: EvaluationsRepository

    @Autowired
    lateinit var responsesRepository: ResponsesRepository

    @Autowired
    lateinit var reportMapper: ReportMapper

    private val reportFolder = "reports" // Carpeta para guardar PDFs

    fun findByEvaluationId(evaluationId: Long): ReportDto? {
        val report = reportRepository.findByEvaluations_Id(evaluationId)
        return report?.let { reportMapper.toReportDto(it) }
    }

    fun delete(id: Long) {
        val report = reportRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Reporte con ID $id no encontrado") }

        val file = File(report.filePath)
        if (file.exists()) file.delete()

        reportRepository.delete(report)
    }

    fun saveByEvaluationId(evaluationId: Long): ReportDto {
        // 1. Asegurar que el resultado global esté calculado
        globalResultsService.calculateAndSaveResult(evaluationId)

        // 2. Buscar evaluación con niño y evaluador
        val evaluation = try {
            evaluationsRepository.findByIdWithChildAndEvaluator(evaluationId)
        } catch (e: Exception) {
            throw EntityNotFoundException("Evaluación con ID $evaluationId no encontrada")
        }

        val child = evaluation.children
        val evaluator = evaluation.evaluator
        val globalResults = globalResultsService.findByEvaluationId(evaluationId)

        // 3. Preparar carpeta y ruta
        File(reportFolder).mkdirs()
        val filePath = "$reportFolder/reporte_evaluacion_${evaluation.id}.pdf"

        // 4. Crear DTO para PDF
        val reportDto = ReportDto(
            id = null,
            evaluationId = evaluation.id!!,
            filePath = filePath,
            generatedAt = LocalDateTime.now(),

            // Niño
            childFullName = child?.fullName ?: "N/A",
            childNui = child?.nui,
            childBirthdate = child?.birthdate?.toString(),
            childGender = child?.gender,

            // Evaluación
            applicationDate = evaluation.applicationDate ?: LocalDateTime.now(),
            chronologicalAgeMonths = evaluation.chronologicalAgeMonths ?: 0,

            // Resultados
            resultYears = globalResults.resultYears,
            resultDetail = globalResults.resultDetail,
            coefficient = globalResults.coefficient?.toDouble(),
            classification = globalResults.classification,
            totalMonthsApproved = globalResults.totalMonthsApproved?.toDouble(),

            // Ítems (no se usarán en PDF, pero se requieren por la clase)
            items = emptyList()
        )

        // 5. Generar el PDF (sin ítems)
        val pdfFile = ReportGenerator.generatePdfWithDetails(reportDto, filePath)

        // 6. Guardar entidad Report
        val report = Report().apply {
            evaluations = evaluation
            this.filePath = pdfFile.absolutePath ?: "desconocido.pdf"
            generatedAt = LocalDateTime.now()
        }

        reportRepository.save(report)
        return reportDto
    }
}