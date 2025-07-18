package com.example.app_brunet_lezine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "evaluators")
class Evaluators {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(name = "speciality", nullable = true)
    var speciality: String? = null

    @Column(name = "full_name", nullable = false)
    var fullName: String? = null

    @Column(name = "nui", nullable = false, unique = true, length = 15)
    var nui: String? = null

    @Column(name = "phone", nullable = true, length = 20)
    var phone: String? = null

    @Column(name = "birthdate", nullable = false)
    var birthdate: LocalDate? = null

    @Column(name = "gender", nullable = true, length = 10)
    var gender: String? = null

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnore
    var user: UserEntity? = null
}
