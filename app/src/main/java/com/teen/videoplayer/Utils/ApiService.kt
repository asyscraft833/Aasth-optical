package com.teen.videoplayer.Utils

import com.teen.videoplayer.Model.AddUserModel
import com.teen.videoplayer.Model.DashBoardFilter
import com.teen.videoplayer.Model.DashBoardUserDetailsResponse
import com.teen.videoplayer.Model.DeleteUserResponse
import com.teen.videoplayer.Model.LoginResponse
import com.teen.videoplayer.Model.MonthlyReportResponse
import com.teen.videoplayer.Model.RegisterResponse
import com.teen.videoplayer.Model.UserImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @Multipart
    @POST("login")
    suspend fun loginUser(
        @Part("mobile") mobile: RequestBody,
        @Part("password") password: RequestBody,
        @Part("mpin") mpin: RequestBody,
    ): Response<LoginResponse>

    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Header("authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("date") date: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): Response<RegisterResponse>


    @GET("user-details")
    suspend fun userDetails(
        @Header("authorization") token: String
    ): Response<DashBoardUserDetailsResponse>


    @GET("monthly")
    suspend fun MonthlyReport(
        @Header("authorization") token: String
    ): Response<MonthlyReportResponse>


    @Multipart
    @POST("user-filter")
    suspend fun userDetailsFilter(
        @Header("Authorization") token: String,
        @Part("search") search: RequestBody
    ): Response<DashBoardFilter>

    @Multipart
    @POST("user-update/{id}")
    suspend fun UpdateUserDetails(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("date") date: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): Response<AddUserModel>

    @DELETE("user-delete/{id}")
    suspend fun DeleteUserDetails(
        @Header("authorization") authorization: String,
        @Path("id") id: String,
    ): Response<DeleteUserResponse>

    @GET("user-gallery/{id}")
    suspend fun userImages(
        @Header("authorization") authorization: String,
        @Path("id") id: String,
    ): Response<UserImageResponse>


}