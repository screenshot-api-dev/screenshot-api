package dev.screenshotapi.core.domain.exceptions

import kotlinx.datetime.Instant

sealed class AuthorizationException(message: String, cause: Throwable? = null) : BusinessException(message, cause) {
    class InsufficientPermissions(val requiredPermission: String) :
        AuthorizationException("Missing permission: $requiredPermission")

    class ApiKeyRequired : AuthorizationException("Valid API key required for screenshot processing")
    class ApiKeyInactive : AuthorizationException("API key is inactive")
    class ApiKeyExpired : AuthorizationException("API key has expired")
    class ApiKeyNotOwned : AuthorizationException("API key does not belong to authenticated user")
    class RateLimitExceeded(val resetTime: Instant) :
        AuthorizationException("Rate limit exceeded. Reset at: $resetTime")
    
    class JobNotAuthorized(message: String = "Not authorized to access this job") :
        AuthorizationException(message)
}
