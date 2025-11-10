package jp.tvq.osaifuplus.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val username:String,
    val password:String
)
@Serializable
data class LoginRequest (
    val email:String,
    val password:String
)
@Serializable
data class AuthResponse (
    val accessToken:String,
    val refreshToken:String,
    val email:String,
    val username:String,
)

@Serializable
data class RefreshRequest (
    val refreshToken:String,
)
@Serializable
data class RefreshResponse (
    val accessToken:String,
    val refreshToken:String,
)
