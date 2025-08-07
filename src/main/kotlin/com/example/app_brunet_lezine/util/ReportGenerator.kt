package com.example.app_brunet_lezine.util

import com.example.app_brunet_lezine.dto.ReportDto
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.time.format.DateTimeFormatter

object ReportGenerator {

    fun generatePdfWithDetails(report: ReportDto, filePath: String): File {
        val file = File(filePath)
        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        document.setMargins(40f, 40f, 60f, 40f)

        // 🟦 Título
        document.add(
            Paragraph("Informe de Evaluación - Brunet Lézine")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18f)
                .setBold()
        )

        document.add(Paragraph("\n"))

        // 👶 Información del niño
        document.add(Paragraph("Información del Niño").setBold().setFontSize(14f))
        document.add(Paragraph("Nombre: ${report.childFullName}"))
        document.add(Paragraph("NUI: ${report.childNui ?: "N/A"}"))
        document.add(Paragraph("Género: ${report.childGender ?: "N/A"}"))
        document.add(Paragraph("Fecha de nacimiento: ${report.childBirthdate ?: "N/A"}"))
        document.add(Paragraph("Edad cronológica: ${report.chronologicalAgeMonths} meses"))
        document.add(Paragraph("Fecha de evaluación: ${formatDate(report.applicationDate)}"))

        document.add(Paragraph("\n"))

        // 📊 Resultados globales
        document.add(Paragraph("Resultados Globales").setBold().setFontSize(14f))
        document.add(Paragraph("Meses aprobados: ${report.totalMonthsApproved ?: "N/A"}"))
        document.add(Paragraph("Coeficiente de desarrollo: ${report.coefficient ?: "N/A"}"))
        document.add(Paragraph("Edad de desarrollo: ${report.resultYears ?: "N/A"}"))
        document.add(Paragraph("Clasificación: ${report.classification ?: "N/A"}"))

        document.add(Paragraph("\n"))

        // 📝 Detalle
        document.add(Paragraph("Detalle del resultado:").setBold())
        document.add(Paragraph(report.resultDetail ?: "No disponible"))

        document.add(Paragraph("\n"))

        // 📌 Observaciones
        if (!report.observaciones.isNullOrBlank()) {
            document.add(Paragraph("Observaciones:").setBold())
            document.add(Paragraph(report.observaciones))
            document.add(Paragraph("\n"))
        }

        // ✍️ FIRMAS
        document.add(Paragraph("\n\n"))

        val signatureTable = Table(UnitValue.createPercentArray(floatArrayOf(1f, 1f)))
            .useAllAvailableWidth()

        signatureTable.addCell(Cell().add(Paragraph("Firma del Evaluador").setBold()).setHeight(50f))
        signatureTable.addCell(Cell().add(Paragraph("Firma del Director del Centro").setBold()).setHeight(50f))

        signatureTable.addCell(Cell().add(Paragraph("_____________________").setTextAlignment(TextAlignment.CENTER)))
        signatureTable.addCell(Cell().add(Paragraph("_____________________").setTextAlignment(TextAlignment.CENTER)))

        document.add(signatureTable)

        document.close()
        return file
    }

    private fun formatDate(date: java.time.LocalDateTime?): String {
        return date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) ?: "N/A"
    }
}
