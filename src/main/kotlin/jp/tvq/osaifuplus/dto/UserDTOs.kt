package jp.tvq.osaifuplus.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserInfor(
    var email:String,
    var username:String
)

