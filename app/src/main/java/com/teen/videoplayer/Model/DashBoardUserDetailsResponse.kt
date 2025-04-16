package com.teen.videoplayer.Model

data class DashBoardUserDetailsResponse(
    val message: String,
    val success: Boolean,
    val user: List<UserDetails>)



data class UserDetails(
    val id: Int,
    val name: String,
    val phone: String,
    val file: String?,
    val created_at: String,
)


data class DashBoardFilter(
    val message: String,
    val success: Boolean,
    val users: List<UserDetails>,
)



