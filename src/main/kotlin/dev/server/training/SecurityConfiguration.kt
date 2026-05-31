package dev.server.training

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity // 💡 Enables @PreAuthorize role guardrails on your endpoints
class SecurityConfiguration(
    // 1. Inject your custom JWT filter via constructor injection
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() } // Tokens are stateless, so CSRF can safely be disabled
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // Clear server-side sessions
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll() // Allow public access to login/register endpoints
//                    .requestMatchers(HttpMethod.GET,"/api/todo").permitAll()
//                    .requestMatchers(HttpMethod.POST, "/api/todo").hasRole("MANAGER")
//                    .requestMatchers(HttpMethod.PUT, "/api/todo/{id}").hasRole("MANAGER")
//                    .requestMatchers(HttpMethod.DELETE, "/api/todo/{id}").hasRole("ADMIN")
                    .anyRequest().authenticated()            // Lock down everything else
            }

            // 2. CRITICAL STEP: Apply your filter right before the default standard auth filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:5173", "http://localhost:3000") // Your React app origins
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type", "Cache-Control")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}