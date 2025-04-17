package com.teen.videoplayer.Model


data class MonthlyReportResponse(
    val message: String,
    val success: Boolean,
    val count: Long,
    val users: List<Usermonthly>,
)

data class Usermonthly(
    val id: Long,
    val name: String,
    val phone: String,
    val file: String,
    val created_at: String,
)
