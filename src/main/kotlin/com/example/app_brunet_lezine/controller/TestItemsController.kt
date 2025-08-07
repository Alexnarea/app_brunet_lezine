package com.example.app_brunet_lezine.controller

import com.example.app_brunet_lezine.dto.TestItemsDto
import com.example.app_brunet_lezine.response.SuccessResponse
import com.example.app_brunet_lezine.service.TestItemsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test-items")
class TestItemsController(
    private val testItemsService: TestItemsService
) {

    @GetMapping
    fun findAll(): ResponseEntity<List<TestItemsDto>> {
        val testItemsList = testItemsService.findAll()
        return ResponseEntity.ok(testItemsList)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<TestItemsDto> {
        val testItem = testItemsService.findById(id)
        return ResponseEntity.ok(testItem)
    }

    @GetMapping("/by-age")
    fun getItemsByAge(@RequestParam age: Int): ResponseEntity<List<TestItemsDto>> {
        val items = testItemsService.findByAge(age)
        return ResponseEntity.ok(items)
    }

    // ✅ Obtener ítems por una lista de IDs
    @PostMapping("/by-ids")
    fun getItemsByIds(@RequestBody ids: List<Long>): ResponseEntity<List<TestItemsDto>> {
        val items = testItemsService.findAllByIds(ids)
        return ResponseEntity.ok(items)
    }

    @PostMapping
    fun create(@Valid @RequestBody testItemsDto: TestItemsDto): ResponseEntity<TestItemsDto> {
        val created = testItemsService.save(testItemsDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody testItemsDto: TestItemsDto): ResponseEntity<TestItemsDto> {
        val updated = testItemsService.update(id, testItemsDto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<SuccessResponse> {
        testItemsService.delete(id)
        return ResponseEntity.ok(
            SuccessResponse(
                data = "TestItem eliminado correctamente",
                message = "Niño eliminado exitosamente"
            )
        )
    }
}
