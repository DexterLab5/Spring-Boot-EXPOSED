package dev.server.training

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, Int> {
    fun existsByUsername(username: String): Boolean

    // Navigates into the roles collection and selects just the 'name' field
    @Query("SELECT r.title FROM Employee e JOIN e.authorities r WHERE e.username = :username")
    fun findRoleNamesByUsername(@Param("username") username: String): Set<String>
}