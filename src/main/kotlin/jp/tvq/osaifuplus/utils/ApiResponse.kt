package jp.tvq.osaifuplus.utils

import kotlinx.serialization.Serializable
import jp.tvq.osaifuplus.dto.AuthResponse

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
