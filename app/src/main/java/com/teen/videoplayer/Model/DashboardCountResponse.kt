package com.teen.videoplayer.Model


data class DashboardCountResponse(
    val message: String,
    val success: Boolean,
    val totaluserscount: Long,
    val weeklyuserscount: Long,
    val monthlyuserscount: Long,
    val totalimagescount: Long,
)
