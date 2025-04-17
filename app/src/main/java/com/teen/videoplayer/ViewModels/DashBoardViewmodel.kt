package com.teen.videoplayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teen.videoplayer.Model.DashBoardFilter
import com.teen.videoplayer.Model.DashBoardUserDetailsResponse
import com.teen.videoplayer.Model.DeleteUserResponse
import com.teen.videoplayer.Model.LoginResponse
import com.teen.videoplayer.Model.MonthlyReportResponse
import com.teen.videoplayer.Model.RegisterResponse
import com.teen.videoplayer.Utils.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class DashBoardViewmodel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var errorlogin = MutableLiveData<Throwable>()
    var progresslogin = MutableLiveData<Boolean>()
    val dashBoardResponse = MutableLiveData<DashBoardUserDetailsResponse>()
    val monthlyReportreponse = MutableLiveData<MonthlyReportResponse>()
    val dashBoardResponseFilter = MutableLiveData<DashBoardFilter>()
    val deleteUserResponse = MutableLiveData<DeleteUserResponse>()


    fun Hitmonthlydata(
        token: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.MonthlyReport(token)

                if (response.isSuccessful) {

                    monthlyReportreponse.postValue(response.body())

                }

            } catch (e: Exception) {
                // Handle error and post it to LiveData
                errorlogin.postValue(e)
            } finally {
                // Hide progress
                progresslogin.value = false
            }
        }
    }

    fun hitDashBoard(
        token: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.userDetails(token)

                if (response.isSuccessful) {

                    dashBoardResponse.postValue(response.body())

                }

            } catch (e: Exception) {
                // Handle error and post it to LiveData
                errorlogin.postValue(e)
            } finally {
                // Hide progress
                progresslogin.value = false
            }
        }
    }

    fun hitDashBoardFilter(
        token: String,
        query: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.userDetailsFilter(token,query)

                if (response.isSuccessful) {
                    dashBoardResponseFilter.postValue(response.body())
                } else {

                    dashBoardResponseFilter.postValue(response.body())
                }


            } catch (e: Exception) {
                // Handle error and post it to LiveData
                errorlogin.postValue(e)
            } finally {
                // Hide progress
                progresslogin.value = false
            }
        }
    }

    fun deleteUserDetails(
        token: String,
        userid: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.deleteUserDetails(token,userid)

                if (response.isSuccessful) {

                    deleteUserResponse.postValue(response.body())

                }

            } catch (e: Exception) {
                // Handle error and post it to LiveData
                errorlogin.postValue(e)
            } finally {
                // Hide progress
                progresslogin.value = false
            }
        }
    }


   }