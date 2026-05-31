package dev.server.training

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val authenticationService: AuthenticationService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getJwtFromRequest(request)

        if (token == null || !tokenProvider.validateToken(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val username = tokenProvider.getUsernameFromToken(token)
        val roles = tokenProvider.getRolesFromToken(token)

        if (!authenticationService.isEmployeeValid(username, roles)) {
            filterChain.doFilter(request, response)
            return
        }

        // Map string roles to Spring Security GrantedAuthority instances
        val authorities = roles.map { SimpleGrantedAuthority(it) }

        val authentication = UsernamePasswordAuthenticationToken(username, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (!bearerToken.isNullOrBlank() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}