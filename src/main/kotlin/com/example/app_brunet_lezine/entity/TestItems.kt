package com.example.app_brunet_lezine.entity

import jakarta.persistence.*

@Entity
@Table(name = "test_items")
class TestItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String? = null

    @Column(name = "reference_age_months", nullable = false)
    var referenceAgeMonths: Int? = null

    @Column(name = "item_order", nullable = false)
    var itemOrder: Int? = null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", nullable = false)
    var domain: Domains? = null

}