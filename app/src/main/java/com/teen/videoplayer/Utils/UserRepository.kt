package com.teen.videoplayer.Utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject


class UserRepository @Inject constructor(private val apiService: ApiService) {


    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun prepareFilePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }



    suspend fun Createuser(
        token: String,
        name: String,
        mobileNumber: String,
        date: String,
        filePart: File? = null
    ) = apiService.registerUser(token,
        createPartFromString(name),
        createPartFromString(mobileNumber),
        createPartFromString(date),
        filePart?.let { prepareFilePart(it) }
    )

    suspend fun updateUserDetails(
        token: String,
        userId: String,
        name: String,
        mobileNumber: String,
        date: String,
        filePart: File? = null
    ) = apiService.UpdateUserDetails(token,userId,
        createPartFromString(name),
        createPartFromString(mobileNumber),
        createPartFromString(date),
        filePart?.let {  prepareFilePart(it) }
    )
    suspend fun deleteUserDetails(
        token: String,
        userId: String,
    ) = apiService.DeleteUserDetails(token,userId)

 suspend fun Userimages(
        token: String,
        userid: String,
    ) = apiService.userImages(token,userid)


    suspend fun Loginuser(
        mobileNumber: String,
        password: String,
        mpin: String,
    ) = apiService.loginUser(
        createPartFromString(mobileNumber),
        createPartFromString(password),
        createPartFromString(mpin),
    )


    suspend fun userDetails(
        token: String,
    ) = apiService.userDetails(token)


  suspend fun userDetailsFilter(
        token: String,
        query:  String,
    ) = apiService.userDetailsFilter(
      token,createPartFromString(query))

}
