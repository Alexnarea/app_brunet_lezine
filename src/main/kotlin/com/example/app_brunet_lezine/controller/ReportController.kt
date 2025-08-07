package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@RestController
@RequestMapping("/api/reports")
class ReportController {

    @Autowired
    lateinit var reportService: ReportService

    @GetMapping("/by-evaluation/{evaluationId}")
    fun findByEvaluationId(@PathVariable evaluationId: Long): ResponseEntity<SuccessResponse> {
        val report = reportService.findByEvaluationId(evaluationId)
        return ResponseEntity.ok(
            SuccessResponse(
                data = report,
                message = "Reporte encontrado"
            )
        )
    }

    // ✅ Este método ahora genera el PDF directamente usando evaluationId
    @PostMapping("/{evaluationId}")
    fun saveByEvaluationId(@PathVariable evaluationId: Long): ResponseEntity<SuccessResponse> {
        val saved = reportService.saveByEvaluationId(evaluationId)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            SuccessResponse(
                data = saved,
                message = "Reporte generado y guardado correctamente"
            )
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        reportService.delete(id)
        return ResponseEntity.ok(SuccessResponse(message = "Reporte eliminado"))
    }

    @GetMapping("/download/{evaluationId}")
    fun downloadPdf(@PathVariable evaluationId: Long): ResponseEntity<Resource> {
        val report = reportService.findByEvaluationId(evaluationId)
            ?: return ResponseEntity.notFound().build()

        val filePath = report.filePath ?: return ResponseEntity.notFound().build()

        val file = FileSystemResource(filePath)
        if (!file.exists()) {
            return ResponseEntity.notFound().build()
        }

        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_evaluacion_${evaluationId}.pdf")
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        }

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.contentLength())
            .body(file)
    }
}
