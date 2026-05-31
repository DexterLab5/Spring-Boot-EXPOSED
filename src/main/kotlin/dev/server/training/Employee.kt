package dev.server.training

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "employee")
class Employee (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null, // -- val = const

    @Column(nullable = false, unique = true, length = 25)
    private var username: String = "",

    @Column(nullable = false, unique = true, length = 25)
    var email: String = "",

    @Column(nullable = false, unique = true, length = 255)
    private var password: String = "",

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_authority",
        joinColumns = [JoinColumn(name = "employee_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
) : UserDetails {

    /**
     * Loops through your MutableSet of Role entities and maps each
     * one's name field (e.g., "Admin") to "ROLE_Admin".
     */
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { role ->
            SimpleGrantedAuthority(role.role)
        }
    }

    override fun getUsername(): String = username
    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}