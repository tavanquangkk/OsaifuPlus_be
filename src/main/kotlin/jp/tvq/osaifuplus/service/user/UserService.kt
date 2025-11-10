package jp.tvq.osaifuplus.service.user

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jp.tvq.osaifuplus.domain.User
import jp.tvq.osaifuplus.repository.UserRepository

@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun getUserByEmail(email:String): User?{
        return userRepository.findByEmail(email)
    }
}