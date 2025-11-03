package jp.tvq.osaifuplus.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jp.tvq.osaifuplus.domain.User

@ApplicationScoped
class UserRepository : PanacheRepository<User>{

    fun findByEmail(email:String): User?{
        return find("email",email).firstResult()
    }
    fun existsByEmail(email:String): Boolean{
        return count("email",email) > 0
    }
}