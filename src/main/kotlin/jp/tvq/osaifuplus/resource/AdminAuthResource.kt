package jp.tvq.osaifuplus.resource

import io.quarkus.security.UnauthorizedException
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.CookieParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.dto.AdminRefreshTokenResponse
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.dto.RefreshRequest
import jp.tvq.osaifuplus.dto.RefreshResponse
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.service.auth.AdminAuthService
import jp.tvq.osaifuplus.service.auth.AuthService
import jp.tvq.osaifuplus.service.jwt.JwtValidator
import jp.tvq.osaifuplus.service.jwt.TokenService
import jp.tvq.osaifuplus.service.user.UserService
import jp.tvq.osaifuplus.utils.ApiResponseAuth
import jp.tvq.osaifuplus.utils.ApiResponseString
import java.time.Duration

@ApplicationScoped
@Path("/admin/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AdminAuthResource {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var adminAuthService: AdminAuthService
    @Inject
    lateinit var tokenService: TokenService
    @Inject lateinit var jwtValidator: JwtValidator


    @POST
    @Path("/login")
    @Transactional
    fun login(loginRequest: LoginRequest): Response {
        return try {

            val authResponse = adminAuthService.login(loginRequest)
            val cookie = NewCookie.Builder("refreshToken")
                .value(authResponse.refreshToken)
                .path("/")
                .comment("Refresh Token")
                .maxAge(60 * 60 * 24 * 30)  // 30 ngày
                .secure(true)               // Chỉ gửi qua HTTPS
                .httpOnly(true)             // Không cho JS đọc token
                .sameSite(NewCookie.SameSite.NONE)  // nếu dùng cross-site (React FE / Quarkus BE)
                .build()

            val successResponse = ApiResponseAuth("success", "ログインに成功しました", authResponse)
            Response.ok(successResponse).cookie(cookie).build()
        } catch (e: WebApplicationException) {
            val errorResponse = ApiResponseString("error", e.message ?: "メールアドレスまたはパスワードが違います", null)
            Response.status(e.response.status).entity(errorResponse).build()
        } catch (e: Exception) {
            val errorResponse = ApiResponseString("error", e.message ?: "不明なエラー", null)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build()
        }
    }

    @POST
    @Path("/logout")
    @Transactional
    fun logout(request: RefreshRequest): Response {
        adminAuthService.logout(request.refreshToken)
        return Response.ok(ApiResponseAuth("success","ログアウトしました")).build()
    }


    @POST
    @Path("/refresh")
    fun refreshToken(
        @CookieParam("refreshToken") refreshToken: String?
    ): Response {

        if (refreshToken.isNullOrBlank()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiResponseAuth("error","refresh　tokenが見つかりませんでした"))
                .build()
        }

        val user = adminAuthService.findByRefreshToken(refreshToken)
            ?: return Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiResponseAuth("error","refresh　tokenに問題がありました"))
                .build()

        // TokenService を使用して新しい Access Token を作る
        val resData = adminAuthService.refreshToken(refreshToken)
        return Response.ok(ApiResponseAuth("success","", resData)).build()
    }

}

