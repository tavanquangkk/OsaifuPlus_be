package jp.tvq.osaifuplus.resource

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jp.tvq.osaifuplus.dto.LoginRequest
import jp.tvq.osaifuplus.dto.RegisterRequest
import jp.tvq.osaifuplus.service.AuthService
import jp.tvq.osaifuplus.utils.ApiResponse

@ApplicationScoped
@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthResource {

    @Inject
    lateinit var authService: AuthService

    @POST
    @Path("/register")
    fun register(request: RegisterRequest): Response{
        val authResponse = authService.register(request)
        return Response.ok(authResponse).build()

    }


    @POST
    @Path("/login")
    fun login (loginRequest: LoginRequest): Response{
        val authResponse = authService.login(loginRequest)
        return Response.ok(authResponse).build()
    }
}