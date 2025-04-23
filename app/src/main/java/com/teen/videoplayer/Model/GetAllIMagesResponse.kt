package com.teen.videoplayer.Model


data class GetAllIMagesResponse(
    val message: String,
    val success: Boolean,
    val data: List<dataAll>,
)


data class dataAll(
    val imageid: Int,
    val date: String,
    val image: String,
)

