package com.teen.videoplayer.Model



data class LoginResponse(
    val success: Boolean,
    val message: String,
    val access_token: String,
    val token_type: String,
)
//
//data class User(
//    val id: Long,
//    val name: String,
//    val phone: String,
//    val file: Any?,
//    val created_at: String,
//)

