package com.example.app_brunet_lezine.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    var id: Long? = null

    @Column(nullable = false, unique = true, length = 20)
    var username: String? = null

    @Column(nullable = false, length = 200)
    var password: String? = null

    @Column(nullable = false, unique = true, length = 50)
    var email: String? = null

    @Column(nullable = false)
    var locked: Boolean = false

    @Column(nullable = false)
    var disabled: Boolean = false
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var roles: MutableList<RoleEntity> = mutableListOf()
}
