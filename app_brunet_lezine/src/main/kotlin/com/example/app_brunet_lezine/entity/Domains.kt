package com.example.app_brunet_lezine.entity

import jakarta.persistence.*

@Entity
@Table(name = "domains")
class Domains {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(updatable = false)
    var id: Long? = null

    var descriptionDomain: String? = null
}