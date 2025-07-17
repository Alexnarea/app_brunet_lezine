package com.example.app_brunet_lezine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "role") // ✅ dejamos el nombre como en tu base
class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(nullable = false)
    var role: String? = null // ✅ mantenemos 'role' como nombre de columna

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // ✅ permitimos insertar el user_id
    @JsonIgnore
    var user: UserEntity? = null
}
