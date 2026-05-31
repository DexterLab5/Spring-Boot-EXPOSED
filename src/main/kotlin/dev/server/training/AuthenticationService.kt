package dev.server.training

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val employeeRepository: EmployeeRepository,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) {

    fun isEmployeeValid(username: String, roles: List<String>): Boolean {
        // Check if the employee exists in the database
        val isUsernameValid = employeeRepository.existsByUsername(username)

        val tokenRolesSet = roles.toSet();
        val employeeRolesSet = employeeRepository.findRoleNamesByUsername(username)
        val areRolesValid = tokenRolesSet == employeeRolesSet

        return isUsernameValid && areRolesValid
    }

    fun login(loginRequest: LoginRequest): AuthResponse {
        // 1. Validate credentials
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        // 2. Extract UserDetails principal
        val employee = authentication.principal as Employee

        // 3. Generate token containing the user roles
        val jwtToken = jwtTokenProvider.generateToken(employee.username, employee.roles.map { it.role })

        return AuthResponse(jwtToken)
    }

    fun register(registerRequest: RegisterRequest): AuthResponse {

        val employeeRole = roleRepository.findByRole("ROLE_EMPLOYEE")

        val newEmployee = Employee(
            username = registerRequest.username,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password)
                ?: throw IllegalStateException("Password encoding failed"), // Crucial for security
            roles = mutableSetOf(employeeRole) // Assign a default role
        )

        // 3. Save the new user to the database
        val savedEmployee = employeeRepository.save(newEmployee)

        // 4. Generate token so they are automatically logged in after registering
        val jwtToken = jwtTokenProvider.generateToken(
            savedEmployee.username,
            savedEmployee.roles.map { it.role }
        )

        return AuthResponse(jwtToken)
    }
}