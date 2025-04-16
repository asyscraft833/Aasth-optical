package com.teen.videoplayer.Model

data class UserImageResponse(
    val message: String,
    val success: Boolean,
    val data: List<data>,
)

data class data(
    val date: String,
    val file: String,
)
