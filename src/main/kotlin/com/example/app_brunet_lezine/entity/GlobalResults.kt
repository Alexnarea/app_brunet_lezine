package com.example.app_brunet_lezine.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "global_results")
class GlobalResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(name = "total_months_approved", nullable = false, precision = 5, scale = 1)
    var totalMonthsApproved: BigDecimal? = null

    @Column(nullable = false, precision = 5, scale = 2)
    var coefficient: BigDecimal? = null

    @Column(name = "result_years", nullable = false, columnDefinition = "TEXT")
    var resultYears: String? = null

    @Column(name = "result_detail", nullable = false, columnDefinition = "TEXT")
    var resultDetail: String? = null

    @Column(nullable = false, length = 50)
    var classification: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false, unique = true)
    var evaluation: Evaluations? = null
}
