package com.example.app_brunet_lezine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.Date

@SpringBootApplication
class AppBrunetLezineApplication

fun main(args: Array<String>) {
	runApplication<AppBrunetLezineApplication>(*args)
	println("Hora actual del servidor: ${Date()}")

}
