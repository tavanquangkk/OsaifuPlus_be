package jp.tvq.osaifuplus.service.user

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject

import jp.tvq.osaifuplus.domain.User
import jp.tvq.osaifuplus.dto.UserInfor
import jp.tvq.osaifuplus.repository.UserRepository


@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun getUserByEmail(email:String): User?{
        return userRepository.findByEmail(email) ?: throw IllegalArgumentException("User not found")
    }

    fun getMyInfo(email: String): UserInfor {
        print("heeee")
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        return UserInfor(user.email,user.username)
    }

}