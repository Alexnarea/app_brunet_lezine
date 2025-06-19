package com.example.app_brunet_lezine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "administrators")
class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(name = "full_name", length = 50, nullable = false)
    var fullName: String? = null

    @Column(length = 15, nullable = false, unique = true)
    var nui: String? = null

    @Column(length = 20)
    var phone: String? = null

    @Column(nullable = false)
    var birthdate: LocalDate? = null

    @Column(length = 10)
    var gender: String? = null

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnore
    var user: UserEntity? = null
}