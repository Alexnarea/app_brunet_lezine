package com.example.app_brunet_lezine.mapper

import com.example.app_brunet_lezine.dto.TestItemsDto
import com.example.app_brunet_lezine.entity.TestItems
import com.example.app_brunet_lezine.entity.Domains
import org.springframework.stereotype.Component

@Component
object TestItemsMapper {

    fun toEntity(testItemsDto: TestItemsDto): TestItems {
        val testItems = TestItems()
        testItems.id = testItemsDto.id
        testItems.description = testItemsDto.description
        testItems.referenceAgeMonths = testItemsDto.referenceAgeMonths
        testItems.itemOrder = testItemsDto.itemOrder
        return testItems
    }

    fun toTestDto(testItems: TestItems): TestItemsDto {
        val testItemsDto = TestItemsDto()
        testItemsDto.id = testItems.id
        testItemsDto.description = testItems.description
        testItemsDto.referenceAgeMonths = testItems.referenceAgeMonths
        testItemsDto.itemOrder = testItems.itemOrder
        testItemsDto.domainId = testItems.domain?.id
        return testItemsDto
    }
}
