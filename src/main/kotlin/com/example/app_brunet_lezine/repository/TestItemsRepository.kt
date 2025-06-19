package com.example.app_brunet_lezine.repository

import com.example.app_brunet_lezine.entity.TestItems
import org.springframework.data.jpa.repository.JpaRepository

interface TestItemsRepository: JpaRepository<TestItems, Long> {
    fun findByReferenceAgeMonthsLessThanEqualOrderByReferenceAgeMonthsAscItemOrderAsc(
        ageInMonths: Int
    ): List<TestItems>
}