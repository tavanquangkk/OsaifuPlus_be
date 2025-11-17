package jp.tvq.osaifuplus.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "transactions")
class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = "",

    @Column(nullable = false)
    var userId: UUID? = null,

    @Column(nullable = false)
    var type: String = "",   // INCOME or EXPENSE

    @Column(nullable = false)
    var amount: Long = 0,

    @Column(nullable = false)
    var category: String = "",

    @Column(nullable = true)
    var note: String? = null,

    @Column(nullable = false)
    var created_at: LocalDateTime = LocalDateTime.now()
)
