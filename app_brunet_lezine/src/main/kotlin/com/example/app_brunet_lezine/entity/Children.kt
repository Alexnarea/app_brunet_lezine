package com.example.app_brunet_lezine.entity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "children")
class Children {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(updatable = false)
    var id: Long? = null

    var fullName: String? = null
    var nui: String? = null
    var birthdate: LocalDate? = null
    var gender: String? = null
    var creationDate: LocalDate? = LocalDate.now()
}