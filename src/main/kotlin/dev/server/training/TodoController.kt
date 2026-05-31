package dev.server.training

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/todo")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getAllTodos(): ResponseEntity<List<Todo>> {
        return ResponseEntity.ok(todoService.getAllTodos())
    }
}