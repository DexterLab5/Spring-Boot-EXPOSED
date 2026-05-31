package dev.server.training

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val employeeRepository: EmployeeRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        // Query the database. Since Employee implements UserDetails, we can return it directly.
        return employeeRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Employee with email $username not found")
    }
}
