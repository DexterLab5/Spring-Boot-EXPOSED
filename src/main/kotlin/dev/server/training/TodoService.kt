package dev.server.training

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class TodoService(
    private val todoRepository: TodoRepository
) {

    fun getAllTodos(): List<Todo> {
        return todoRepository.findAll()
    }

    fun getTodoById(id: Int): Todo {
        return todoRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("404: Todo Not found")
    }

    fun addTodo(todo: Todo): Todo {
        return todoRepository.save(todo)
    }

    fun updateTodo(id: Int, updateTodoRequest: UpdateTodoRequest): Todo {
        val existingTodo = getTodoById(id)

        // Map fields from updatedTodo to existingTodo
        existingTodo.title = updateTodoRequest.title
        existingTodo.description = updateTodoRequest.description

        return todoRepository.save(existingTodo)
    }

    fun deleteTodo(id: Int) {
        todoRepository.deleteById(id)
    }

    fun completeTodo(id: Int): Todo {
        val todo = getTodoById(id)
        todo.isCompleted = true
        return todoRepository.save(todo)
    }
}