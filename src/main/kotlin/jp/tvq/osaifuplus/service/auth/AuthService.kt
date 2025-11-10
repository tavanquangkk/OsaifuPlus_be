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
import jp.tvq.osaifuplus.dto.RefreshRequest
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.repository.UserRepository
import jp.tvq.osaifuplus.service.PasswordUtil
import jp.tvq.osaifuplus.service.jwt.JwtValidator
import jp.tvq.osaifuplus.service.jwt.TokenService

@ApplicationScoped
class AuthService {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var passwordUtil: PasswordUtil

    @Inject
    lateinit var tokenService: TokenService

    @Inject
    lateinit var jwtValidator: JwtValidator


    // Register
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if(userRepository.existsByEmail(request.email)){
            throw WebApplicationException("このメールアドレスは既に使用されています", Response.Status.CONFLICT)
        }
        val passwordHash = passwordUtil.hassPassword(request.password)
        val newUser = User().apply {
            this.email = request.email
            this.username = request.username
            this.password = passwordHash
        }
        userRepository.persist(newUser)
        // データベースとメモリを同期し、ID (user_id) を取得
        userRepository.flush()
        // Generate JWT
        val accessToken = tokenService.generateAccessToken(user = newUser)
        val refreshAccessToken = tokenService.generateRefreshAccessToken(newUser)
        newUser.refreshToken = refreshAccessToken
        return AuthResponse(
            accessToken, refreshAccessToken, newUser.email, newUser.username
        )
    }

    // Login

    fun login(request: LoginRequest): AuthResponse {
        // check user exist
       val currentUser = userRepository.findByEmail(request.email) ?: throw WebApplicationException(
           "メールアドレスもしくはパスワードが正しくありません",
           Response.Status.UNAUTHORIZED
       )

        // 2. パスワードを照合
        if (!passwordUtil.checkPassword(request.password, currentUser.password)) {
            throw WebApplicationException(
                "メールアドレスまたはパスワードが正しくありません",
                Response.Status.UNAUTHORIZED
            )
        }
        // Generate JWT
        val accessToken = tokenService.generateAccessToken(currentUser)
        val refreshAccessToken = tokenService.generateRefreshAccessToken(currentUser)
        currentUser.refreshToken=refreshAccessToken
        userRepository.persist(currentUser)
        return AuthResponse(
            accessToken, refreshAccessToken, currentUser.email, currentUser.username
        )


    }

    // check refreshToken

    fun checkValidRefreshToken(refreshToken:String): Boolean{

        val jwt = jwtValidator.validateToken(refreshToken) ?: return false
        val currentUser = userRepository.findByRefreshToken(refreshToken);
        return currentUser != null

    }

    //refreshToken
    @Transactional
    fun refreshToken(request: RefreshRequest): AuthResponse {
        val jwt = jwtValidator.validateToken(request.refreshToken)
            ?: throw UnauthorizedException("無効なトークン")

        val user = userRepository.findByRefreshToken(request.refreshToken)
            ?: throw UnauthorizedException("トークンが無効です")

        val newAccessToken = tokenService.generateAccessToken(user)
        val newRefreshToken = tokenService.generateRefreshAccessToken(user)

        // refreshToken 更新
        user.refreshToken = newRefreshToken
        userRepository.persist(user)
        userRepository.flush() // DB への反映を確実に

        return AuthResponse(newAccessToken, newRefreshToken, user.email, user.username)
    }





}