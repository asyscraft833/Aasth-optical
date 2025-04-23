package com.teen.videoplayer.Model


data class LoginResponse(
    val success: Boolean,
    val message: String,
    val access_token: String,
    val token_type: String,
    val username: String,
    val mobile: String,
    val total_user: Long,
)


