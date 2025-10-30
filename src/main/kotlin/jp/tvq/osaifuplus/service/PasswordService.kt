package jp.tvq.osaifuplus.service

import io.quarkus.elytron.security.common.BcryptUtil
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PasswordUtil {
    fun hassPassword (password:String):String{
        return BcryptUtil.bcryptHash(password)
    }
    fun checkPassword (plainPassword:String,hashedPassword :String): Boolean{
        return BcryptUtil.matches(plainPassword,hashedPassword)
    }

}