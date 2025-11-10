package jp.tvq.osaifuplus.resource

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.dto.RefreshRequest
import jp.tvq.osaifuplus.dto.RefreshResponse
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.service.auth.AuthService
import jp.tvq.osaifuplus.service.jwt.JwtValidator
import jp.tvq.osaifuplus.service.jwt.TokenService
import jp.tvq.osaifuplus.service.user.UserService
import jp.tvq.osaifuplus.utils.ApiResponseAuth
import jp.tvq.osaifuplus.utils.ApiResponseString

@ApplicationScoped
@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthResource {

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var userService: UserService
    @Inject lateinit var jwtValidator: JwtValidator

    @POST
    @Path("/register")
    fun register(request: RegisterRequest): Response {
        return try {
            val authResponse = authService.register(request)
            val successResponse = ApiResponseAuth("success", "作成に成功しました", authResponse)
            Response.ok(successResponse).build()
        } catch (e: WebApplicationException) {
            val errorResponse = ApiResponseString("error", e.message ?: "作成に失敗しました", null)
            Response.status(e.response.status).entity(errorResponse).build()
        } catch (e: Exception) {
            val errorResponse = ApiResponseString("error", e.message ?: "不明なエラー", null)
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build()
        }
    }


    @POST
    @Path("/login")
    @Transactional
    fun login(loginRequest: LoginRequest): Response {
        return try {
            val authResponse = authService.login(loginRequest)
            val successResponse = ApiResponseAuth("success", "ログインに成功しました", authResponse)
            Response.ok(successResponse).build()
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
    fun logout(): Response{


        return Response.ok(ApiResponseAuth("success","ログアウトしました")).build()
    }


    @POST
    @Path("/refresh")
    @Transactional
    fun refreshToken(request: RefreshRequest): Response {
        return try {
            val resData = authService.refreshToken(request)
            Response.ok(ApiResponseAuth("success","新しいトークンが発行されました", resData)).build()
        } catch (e: UnauthorizedException) {
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(ApiResponseAuth("error", e.message ?: "リフレッシュトークンが無効です", null)).build()
        } catch (e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponseAuth("error", e.message ?: "不明なエラー", null)).build()
        }
    }

}

