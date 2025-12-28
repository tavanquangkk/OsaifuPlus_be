package jp.tvq.osaifuplus.utils

import kotlinx.serialization.Serializable
import jp.tvq.osaifuplus.dto.AuthResponse
import jp.tvq.osaifuplus.dto.MonthlySeparatedSummary
import jp.tvq.osaifuplus.dto.TransactionResponse
import jp.tvq.osaifuplus.dto.UserInfor

// ジェネリック型のままだとシリアライゼーションで問題が発生するため
// 具体的な型のレスポンスクラスを定義

@Serializable
data class ApiResponseString(
    val status: String,
    val message: String,
    val data: String? = null
)

@Serializable
data class ApiResponseAuth(
    val status: String,
    val message: String,
    val data: AuthResponse? = null
)

@Serializable
data class ApiResponseUserInfo(
    val status: String,
    val message: String,
    val data: UserInfor? = null
)

@Serializable
data class ApiResponseTrasaction(
    val status: String,
    val message: String,
    val data: TransactionResponse? = null
)
@Serializable
data class ApiResponseAllTrasactions(
    val status: String,
    val message: String,
    val data: List<TransactionResponse>? = null
)

@Serializable
data class ApiResponseMonthlySeparatedSummary(
    val status: String,
    val message: String,
    val data: MonthlySeparatedSummary?
)

