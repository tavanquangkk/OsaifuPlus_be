package jp.tvq.osaifuplus.utils

import java.util.Objects

data class ApiResponse
(
    var status:String,
    var message:String,
    var data: Objects
)