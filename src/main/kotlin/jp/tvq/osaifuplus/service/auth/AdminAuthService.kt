package jp.tvq.osaifuplus.service.auth


import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.domain.User
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.repository.UserRepository
import jp.tvq.osaifuplus.service.PasswordUtil
import jp.tvq.osaifuplus.service.jwt.JwtValidator
import jp.tvq.osaifuplus.service.jwt.TokenService

@ApplicationScoped
class AdminAuthService {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var passwordUtil: PasswordUtil

    @Inject
    lateinit var tokenService: TokenService

    @Inject
    lateinit var jwtValidator: JwtValidator

    // Login
    @Transactional
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw WebApplicationException(
                "メールアドレスまたはパスワードが違います",
                Response.Status.UNAUTHORIZED
            )

        if (!passwordUtil.checkPassword(request.password, user.password)) {
            throw WebApplicationException(
                "メールアドレスまたはパスワードが違います",
                Response.Status.UNAUTHORIZED
            )
        }

        if (user.role != "ADMIN") {
            throw WebApplicationException(
                "管理者権限がありません",
                Response.Status.FORBIDDEN
            )
        }

        val accessToken = tokenService.generateAccessToken(user)
        val refreshToken = tokenService.generateRefreshAccessToken(user)
        user.refreshToken = refreshToken
        userRepository.persist(user)

        return AuthResponse(accessToken, refreshToken, user.email, user.username)
    }

    // Refresh Token
    @Transactional
    fun refreshToken(refreshToken: String?): AuthResponse {
        if (refreshToken.isNullOrBlank()) {
            throw UnauthorizedException("Refresh token がありません")
        }

        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw UnauthorizedException("無効な refresh token")

        if (user.role != "ADMIN") {
            throw UnauthorizedException("管理者権限がありません")
        }

        val newAccessToken = tokenService.generateAccessToken(user)

        return AuthResponse(newAccessToken, null, user.email, user.username)
    }

    // Logout
    @Transactional
    fun logout(refreshToken: String?) {
        if (refreshToken.isNullOrBlank()) {
            throw UnauthorizedException("Refresh token がありません")
        }

        val user = userRepository.findByRefreshToken(refreshToken)
            ?: throw UnauthorizedException("無効な refresh token")

        if (user.role != "ADMIN") {
            throw UnauthorizedException("管理者権限がありません")
        }

        user.refreshToken = null
        userRepository.persist(user)
    }
    // get User by refreshToken
    fun findByRefreshToken(refreshToken: String): User? {
        return userRepository.find("refreshToken", refreshToken).firstResult()
    }
}

