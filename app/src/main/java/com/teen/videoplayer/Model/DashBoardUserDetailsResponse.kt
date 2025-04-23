package com.teen.videoplayer.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class DashBoardUserDetailsResponse(
    val message: String,
    val success: Boolean,
    val user: List<UserDetails>,
)

@Parcelize
data class UserDetails(
    val id: Long,
    val name: String,
    val phone: String,
    val images: List<Image>,
) : Parcelable

@Parcelize
data class Image(
    val imageid: Int,
    val date: String,
    val file: String,
) : Parcelable


// search data api response

data class DashBoardFilter(
    val message: String,
    val success: Boolean,
    val users: List<UserDetails>,
)

data class MonthlyReportResponse(
    val message: String,
    val success: Boolean,
    val count: Long,
    val users: List<UserDetails>,
)



