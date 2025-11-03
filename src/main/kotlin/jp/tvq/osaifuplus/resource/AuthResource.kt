package jp.tvq.osaifuplus.resource

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.service.AuthService
import jp.tvq.osaifuplus.utils.ApiResponseAuth
import jp.tvq.osaifuplus.utils.ApiResponseString

@ApplicationScoped
@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthResource {

    @Inject
    lateinit var authService: AuthService

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
}

