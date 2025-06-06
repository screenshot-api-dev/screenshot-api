package dev.screenshotapi.infrastructure.adapters.input.rest.dto

import dev.screenshotapi.core.usecases.auth.AuthenticateUserResponse
import dev.screenshotapi.core.usecases.auth.CreateApiKeyResponse
import dev.screenshotapi.core.usecases.auth.GetUserProfileResponse
import dev.screenshotapi.core.usecases.auth.RegisterUserResponse
import kotlinx.serialization.Serializable

// Request DTOs
@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String? = null
)

@Serializable
data class UpdateProfileRequestDto(
    val name: String? = null,
    val email: String? = null
)

@Serializable
data class CreateApiKeyRequestDto(
    val name: String,
    val permissions: List<String>? = null
)

// Response DTOs
@Serializable
data class LoginResponseDto(
    val token: String,
    val userId: String,
    val email: String,
    val name: String?,
    val expiresAt: String
)

@Serializable
data class RegisterResponseDto(
    val userId: String,
    val email: String,
    val name: String?,
    val status: String,
    val planId: String,
    val creditsRemaining: Int
)

@Serializable
data class UserProfileResponseDto(
    val userId: String,
    val email: String,
    val name: String?,
    val status: String,
    val planId: String,
    val creditsRemaining: Int,
    val createdAt: String,
    val lastActivity: String?
)

@Serializable
data class ApiKeyResponseDto(
    val id: String,
    val name: String,
    val keyValue: String,
    val isActive: Boolean,
    val createdAt: String
)

@Serializable
data class ApiKeysListResponseDto(
    val apiKeys: List<ApiKeySummaryResponseDto>
)

@Serializable
data class ApiKeySummaryResponseDto(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val maskedKey: String,
    val usageCount: Long,
    val createdAt: String,
    val lastUsedAt: String?
)

@Serializable
data class UserUsageResponseDto(
    val userId: String,
    val creditsRemaining: Int,
    val totalScreenshots: Long,
    val screenshotsLast30Days: Long,
    val planId: String,
    val currentPeriodStart: String,
    val currentPeriodEnd: String
)

// Extension functions for conversion
fun AuthenticateUserResponse.toDto(): LoginResponseDto = LoginResponseDto(
    token = token ?: "jwt_token_placeholder",
    userId = userId ?: "unknown_user",
    email = email ?: "unknown@example.com",
    name = null,
    expiresAt = "placeholder_expiry"
)

fun RegisterUserResponse.toDto(): RegisterResponseDto = RegisterResponseDto(
    userId = userId ?: "unknown_user",
    email = email ?: "unknown@example.com",
    name = null,
    status = status?.name?.lowercase() ?: "active",
    planId = "basic_plan",
    creditsRemaining = 100
)

fun GetUserProfileResponse.toDto(): UserProfileResponseDto = UserProfileResponseDto(
    userId = userId,
    email = email,
    name = name,
    status = status.name.lowercase(),
    planId = "basic_plan",
    creditsRemaining = creditsRemaining,
    createdAt = "2023-01-01T00:00:00Z",
    lastActivity = null
)

fun CreateApiKeyResponse.toDto(): ApiKeyResponseDto = ApiKeyResponseDto(
    id = id,
    name = name,
    keyValue = keyValue,
    isActive = isActive,
    createdAt = createdAt
)

fun dev.screenshotapi.core.usecases.auth.GetUserUsageResponse.toDto(): UserUsageResponseDto = UserUsageResponseDto(
    userId = userId,
    creditsRemaining = creditsRemaining,
    totalScreenshots = totalScreenshots,
    screenshotsLast30Days = screenshotsLast30Days,
    planId = planId,
    currentPeriodStart = currentPeriodStart.toString(),
    currentPeriodEnd = currentPeriodEnd.toString()
)
