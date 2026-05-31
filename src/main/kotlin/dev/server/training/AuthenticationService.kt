package dev.server.training

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val employeeRepository: EmployeeRepository) {

    fun isEmployeeValid(username: String, roles: List<String>): Boolean {
        // Check if the employee exists in the database
        val isUsernameValid = employeeRepository.existsByUsername(username)

        val tokenRolesSet = roles.toSet();
        val employeeRolesSet = employeeRepository.findRoleNamesByUsername(username)
        val areRolesValid = tokenRolesSet == employeeRolesSet

        return isUsernameValid && areRolesValid
    }
}