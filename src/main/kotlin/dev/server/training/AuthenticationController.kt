package dev.server.training

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(val username: String, val password: String)
data class AuthResponse(val token: String)
data class RegisterRequest(val username: String, val email: String, val password: String)

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val employeeRepository: EmployeeRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        // 1. Validate credentials
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )

        // 2. Extract UserDetails principal
        val employee = authentication.principal as Employee

        // 3. Generate token containing the user roles
        val jwtToken = jwtTokenProvider.generateToken(employee.username, employee.roles.map { it.role })

        // 4. Return response
        return ResponseEntity.ok(AuthResponse(jwtToken))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        // 1. Check if username/email already exists
//        if (employeeRepository.existsByUsername(request.username)) {
//            return ResponseEntity.badRequest().body(AuthResponse("Username is already taken")) // Or throw a custom exception
//        } -- Hibernate will check that and email

        // 2. Map request to Employee entity and encode the password
        val newEmployee = Employee(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password)
                ?: throw IllegalStateException("Password encoding failed"), // Crucial for security
            roles = mutableSetOf(Role(role = "ROLE_EMPLOYEE")) // Assign a default role
        )

        // 3. Save the new user to the database
        val savedEmployee = employeeRepository.save(newEmployee)

        // 4. Generate token so they are automatically logged in after registering
        val jwtToken = jwtTokenProvider.generateToken(
            savedEmployee.username,
            savedEmployee.roles.map { it.role }
        )

        // 5. Return response
        return ResponseEntity.ok(AuthResponse(jwtToken))
    }

}