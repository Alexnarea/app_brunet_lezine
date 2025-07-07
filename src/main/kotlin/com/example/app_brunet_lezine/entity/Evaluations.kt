package com.example.app_brunet_lezine.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "evaluations")
class Evaluations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(name = "application_date", nullable = false)
    var applicationDate: LocalDateTime? = LocalDateTime.now()

    @Column(name = "chronological_age_months", nullable = false)
    var chronologicalAgeMonths: Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "children_id", nullable = false)
    var children: Children? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", nullable = false)
    var evaluator: Evaluators? = null

    @OneToOne(mappedBy = "evaluation", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var globalResults: GlobalResults? = null

}