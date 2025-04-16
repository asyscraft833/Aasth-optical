package com.teen.videoplayer.Model



//data class RegisterResponse(
//    val success: Boolean,
//    val message: String,
//    val user: Userdata,
//)
//
//data class Userdata(
//    val id: Long,
//    val name: String,
//    val phone: String,
//    val file: Any?,
//    val created_at: String,
//)


data class RegisterResponse(
    val name: String,
    val mobile: String,
    val date: String,
    val file: File,
)

data class File(
    val original_name: String,
    val mime_type: String,
    val size: Long,
)
