package jp.tvq.osaifuplus.service.jwt

import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jp.tvq.osaifuplus.domain.User
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.jwt.JsonWebToken
import java.time.Instant

@ApplicationScoped
class TokenService {
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    lateinit var issuer :String

    @Inject
    lateinit var jwt : JsonWebToken


    fun generateAccessToken(user: User):String{
        val groups = setOf<String>(user.role.toString())
        return Jwt.claims()
            .issuer(issuer)
            .subject(user.user_id.toString())
            .upn(user.email.toString())
            .groups(groups)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(15))
            .sign()

    }
    fun generateRefreshAccessToken(user: User):String{
        val groups = setOf<String>(user.role.toString())
        return Jwt.claims()
            .issuer(issuer)
            .subject(user.user_id.toString())
            .upn(user.email.toString())
            .groups(groups)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(43200))  //30days
            .sign()

    }
}