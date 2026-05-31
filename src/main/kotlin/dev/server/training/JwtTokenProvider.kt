package dev.server.training

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.jwt.expiration-ms}") private val jwtExpirationInMs: Long
) {
    // Auth0 uses its own Algorithm helper object
    private val algorithm: Algorithm = Algorithm.HMAC256(jwtSecret)


    fun generateToken(username: String, roles: List<String>): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return JWT.create()
            .withSubject(username)
            .withClaim("roles", roles) // Auth0 handles list claims natively
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(algorithm) // Signs via com.auth0 algorithm object
    }

    fun getUsernameFromToken(token: String): String {
        val decodedJWT = decodeAndVerify(token)
        return decodedJWT.subject
    }

    fun getRolesFromToken(token: String): List<String> {
        val decodedJWT = decodeAndVerify(token)
        // Extract array claim as a list of strings cleanly
        return decodedJWT.getClaim("roles").asList(String::class.java) ?: emptyList()
    }

    fun validateToken(token: String): Boolean {
        return try {
            decodeAndVerify(token)
            true
        } catch (ex: Exception) {
            false // Catches SignatureVerificationException or TokenExpiredException
        }
    }

    // Helper utility method to execute the verification pipeline
    private fun decodeAndVerify(token: String): DecodedJWT {
        val verifier = JWT.require(algorithm).build() // Creates reusable verifier pipeline
        return verifier.verify(token)
    }
}