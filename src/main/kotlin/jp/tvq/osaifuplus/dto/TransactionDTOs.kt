package jp.tvq.osaifuplus.dto

import jp.tvq.osaifuplus.utils.transactionType.TransactionType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class TransactionRequest(
    var type: String,
    var amount: Long,
    var category:String,
    var note:String,
)

@Serializable
data class TransactionResponse(
    var id: String,
    var type: String,
    var amount: Long,
    var category: String,
    var note: String?,
    @Contextual
    var created_at:  String?
)
