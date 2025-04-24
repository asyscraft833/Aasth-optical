package com.teen.videoplayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teen.videoplayer.Model.DashBoardFilter
import com.teen.videoplayer.Model.DashBoardUserDetailsResponse
import com.teen.videoplayer.Model.DashboardCountResponse
import com.teen.videoplayer.Model.DeleteUserResponse
import com.teen.videoplayer.Model.GetAllIMagesResponse
import com.teen.videoplayer.Model.MonthlyReportResponse
import com.teen.videoplayer.Utils.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashBoardViewmodel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var errorlogin = MutableLiveData<Throwable>()
    var progresslogin = MutableLiveData<Boolean>()
    val dashBoardResponse = MutableLiveData<DashBoardUserDetailsResponse>()
    val dashBoardResponsecounts = MutableLiveData<DashboardCountResponse>()
    val monthlyReportreponse = MutableLiveData<MonthlyReportResponse>()
    val dashBoardResponseFilter = MutableLiveData<DashBoardFilter>()
    val deleteUserResponse = MutableLiveData<DeleteUserResponse>()
    val GetAllIMagesResponseList = MutableLiveData<GetAllIMagesResponse>()


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

    fun Hitweeklydata(
        token: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.WeeklyReport(token)

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

    fun hitGalleryImage(
        token: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.hitGalleryImage(token)

                if (response.isSuccessful) {

                    GetAllIMagesResponseList.postValue(response.body())

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

    fun HitDashboardCount(
        token: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.DashboardCount(token)

                if (response.isSuccessful) {

                    dashBoardResponsecounts.postValue(response.body())

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
                val response = repository.userDetailsFilter(token, query)

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
                val response = repository.deleteUserDetails(token, userid)

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


    fun deleteUserImages(
        token: String,
        Imageid: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true
                val response = repository.deleteUserImages(token, Imageid)

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