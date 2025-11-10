package jp.tvq.osaifuplus.service.jwt

import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.auth.principal.ParseException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.jwt.JsonWebToken

@ApplicationScoped
class JwtValidator {

    @Inject
    lateinit var jwtParser: JWTParser

    fun validateToken(token:String): JsonWebToken?{
        return try {
            val jwt = jwtParser.parse(token)
            jwt
        } catch (e: ParseException){
            println("❌ Invalid JWT: ${e.message}")
            null
        } catch (e : Exception){
            println("❌ Unexpected error: ${e.message}")
            null
        }
    }
}