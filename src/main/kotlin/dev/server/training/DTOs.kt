package dev.server.training

data class LoginRequest(val username: String, val password: String) {
    init {
        require(username.isNotBlank()) { "Username cannot be empty" }
        require(password.isNotBlank()) { "Password cannot be empty" }
    }
}

data class RegisterRequest(val username: String, val email: String, val password: String) {
    init {
        require(username.isNotBlank()) { "Username cannot be empty" }
        require(email.isNotBlank()) { "Email cannot be empty" }
        require(password.isNotBlank()) { "Password cannot be empty" }
    }
}

data class AuthResponse(val token: String)

data class UpdateTodoRequest(val title: String, val description: String)