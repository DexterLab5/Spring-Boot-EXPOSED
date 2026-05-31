package dev.server.training

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrainingApplication

fun main(args: Array<String>) {
    runApplication<TrainingApplication>(*args)
}
