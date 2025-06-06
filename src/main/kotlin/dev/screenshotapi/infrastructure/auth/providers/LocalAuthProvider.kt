package dev.screenshotapi.infrastructure.auth.providers

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import dev.screenshotapi.core.domain.entities.AuthResult
import dev.screenshotapi.core.domain.services.AuthProvider
import dev.screenshotapi.core.domain.repositories.UserRepository

class LocalAuthProvider(
    private val userRepository: UserRepository,
    private val jwtSecret: String,
    private val jwtIssuer: String,
    private val jwtAudience: String
) : AuthProvider {
    
    override val providerName: String = "local"
    
    private val algorithm = Algorithm.HMAC256(jwtSecret)
    
    private val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(jwtAudience)
        .withIssuer(jwtIssuer)
        .build()
    
    override suspend fun validateToken(token: String): AuthResult? {
        return try {
            val jwt = verifier.verify(token)
            val userId = jwt.getClaim("userId")?.asString() ?: return null
            
            val user = userRepository.findById(userId) ?: return null
            
            AuthResult(
                userId = user.id,
                email = user.email,
                name = user.name,
                providerId = user.id,
                providerName = providerName
            )
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun createUserFromToken(token: String): AuthResult? {
        // For local provider, we don't create users from tokens
        // Users are created through registration flow
        return validateToken(token)
    }
    
    fun createToken(userId: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtIssuer)
            .withClaim("userId", userId)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
            .sign(algorithm)
    }
}