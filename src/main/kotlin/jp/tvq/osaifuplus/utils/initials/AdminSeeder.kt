package jp.tvq.osaifuplus.utils.initials

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jp.tvq.osaifuplus.domain.User
import jp.tvq.osaifuplus.repository.UserRepository
import jp.tvq.osaifuplus.service.PasswordUtil

@ApplicationScoped
class AdminSeeder @Inject constructor(
    val userRepository: UserRepository,
    val passwordUtil: PasswordUtil
) {

    @Transactional
    @PostConstruct
    fun init() {
        val email = "admin@gmail.com"
        if (!userRepository.existsByEmail(email)) {
            val admin = User().apply {
                this.email = email
                this.username = "Admin"
                this.password = passwordUtil.hassPassword("admin123") // 適切にハッシュ
                this.role = "ADMIN"
            }
            userRepository.persist(admin)
        }
    }
}
