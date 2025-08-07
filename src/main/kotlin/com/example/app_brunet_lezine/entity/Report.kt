package com.example.app_brunet_lezine.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reports")
class Report{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(name = "file_path", nullable = false, length = 255)
    var filePath: String? = null

    @Column(name = "generated_at")
    var generatedAt: LocalDateTime? = LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    var evaluations: Evaluations? = null
}
