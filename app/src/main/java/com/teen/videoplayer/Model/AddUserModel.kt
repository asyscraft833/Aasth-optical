package com.teen.videoplayer.Model

data class AddUserModel(
val success: Boolean,
val message: String,
val user: User,
)

data class User(
    val id: Long,
    val name: String,
    val phone: String,
    val file: String,
)

