package jp.tvq.osaifuplus.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.domain.User
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.repository.UserRepository

@ApplicationScoped
class AuthService {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var passwordUtil: PasswordUtil

    @Inject
    lateinit var tokenService: TokenService


    // Register
    @Transactional
    fun register(request: RegisterRequest): AuthResponse{
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
        return AuthResponse(
            accessToken, refreshAccessToken,newUser.email,newUser.username
        )
    }

    // Login

    fun login(request: LoginRequest): AuthResponse{
        // check user exist
       val currentUser = userRepository.findByEmail(request.email) ?: throw WebApplicationException("メールアドレスもしくはパスワードが正しくありません",
           Response.Status.UNAUTHORIZED)

        // 2. パスワードを照合
        if (!passwordUtil.checkPassword(request.password, currentUser.password)) {
            throw WebApplicationException("メールアドレスまたはパスワードが正しくありません", Response.Status.UNAUTHORIZED)
        }
        // Generate JWT
        val accessToken = tokenService.generateAccessToken(currentUser)
        val refreshAccessToken = tokenService.generateRefreshAccessToken(currentUser)
        return AuthResponse(
            accessToken, refreshAccessToken,currentUser.email,currentUser.username
        )


    }


}