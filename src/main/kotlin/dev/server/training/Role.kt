package dev.server.training

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "role")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    // Moving 'role' here creates the constructor you need!
    @Column(nullable = false, unique = true, length = 25, updatable = false)
    val role: String = ""
)