package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.Evaluators
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EvaluatorsRepository: JpaRepository<Evaluators, Long> {
}