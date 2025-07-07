package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Evaluators
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface EvaluatorsRepository: JpaRepository<Evaluators, Long> {
    @Query("SELECT e FROM Evaluators e WHERE e.user.username = :username")
    fun findByUserUsername(@Param("username") username: String): Evaluators?
    fun findByUserId(userId: Long): Evaluators?

}