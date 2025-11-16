    package jp.tvq.osaifuplus.resource

    import jakarta.enterprise.context.ApplicationScoped
    import jakarta.inject.Inject
    import jakarta.ws.rs.GET
    import jakarta.ws.rs.Path
    import jakarta.ws.rs.core.Response
    import jp.tvq.osaifuplus.domain.User
    import jp.tvq.osaifuplus.dto.UserInfor
    import jp.tvq.osaifuplus.service.user.UserService
    import jp.tvq.osaifuplus.utils.ApiResponseUserInfo
    import org.eclipse.microprofile.jwt.JsonWebToken


    @ApplicationScoped
    @Path("/api/v1/users")
    class UserResource {

        @Inject
        lateinit var userService: UserService

        @Inject
        lateinit var jwt: JsonWebToken


        @GET
        @Path("/me")
        fun getMyInfo(): Response? {
//            val email = jwt.claim<String>("email").orElse(null)
//                ?: throw IllegalStateException("JWTにemailクレームが含まれていません")
            val email = jwt.name
            return try {
                val me = userService.getMyInfo(email)
                val successResponse = ApiResponseUserInfo("success", "情報の取得に成功しました", me)

                Response.ok(successResponse).build()
            } catch (e: Exception) {
                val errorResponse = ApiResponseUserInfo("error", "情報が見つかりませんでした", null)

                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build()
            }
        }
    }