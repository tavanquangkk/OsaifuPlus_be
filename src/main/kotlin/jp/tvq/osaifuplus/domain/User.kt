package jp.tvq.osaifuplus.domain
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable


@Entity
@Table(name = "users")
open class User {
   @Id @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var user_id:String
    lateinit var email: String
    lateinit var username:String
    lateinit var password: String
     var role:String? = "USER"
    @Column(name = "refreshToken",  columnDefinition = "text")
    var refreshToken:String? = ""
}

