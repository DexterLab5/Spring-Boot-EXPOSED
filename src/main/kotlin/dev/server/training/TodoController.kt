package dev.server.training

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/todo")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getAllTodos(): ResponseEntity<List<Todo>> {
        print("GETTING ALL TODOS")
        return ResponseEntity.ok(todoService.getAllTodos())
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Int): ResponseEntity<Todo> {
        return ResponseEntity.ok(todoService.getTodoById(id))
    }

    @PostMapping
    fun addTodo(@RequestBody todo: Todo): ResponseEntity<Todo> {
        return ResponseEntity.ok(todoService.addTodo(todo))
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Int, @RequestBody todoRequest: UpdateTodoRequest): ResponseEntity<Todo> {
        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest))
    }

    @DeleteMapping("{id}")
    fun deleteTodo(@PathVariable id: Int): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{id}/complete")
    fun completeTodo(@PathVariable id: Int): ResponseEntity<Todo> {
        return ResponseEntity.ok(todoService.completeTodo(id))
    }

}