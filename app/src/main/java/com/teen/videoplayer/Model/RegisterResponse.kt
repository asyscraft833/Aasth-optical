package com.teen.videoplayer.Model


data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: Userdata,
)

data class Userdata(
    val id: Long,
    val name: String,
    val phone: String,
    val images: List<Any?>,
)

