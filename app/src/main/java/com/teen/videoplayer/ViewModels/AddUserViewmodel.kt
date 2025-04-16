package com.teen.videoplayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teen.videoplayer.Model.AddUserModel
import com.teen.videoplayer.Model.LoginResponse
import com.teen.videoplayer.Model.RegisterResponse
import com.teen.videoplayer.Model.UserImageResponse
import com.teen.videoplayer.Utils.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class AddUserViewmodel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var errorlogin = MutableLiveData<Throwable>()
    var progresslogin = MutableLiveData<Boolean>()
    val userRegisterResponse = MutableLiveData<RegisterResponse>()
    val userRegisterUpdateResponse = MutableLiveData<AddUserModel>()
    val UserImageResponseResponse = MutableLiveData<UserImageResponse>()



    fun hitCreateUser(
        token: String,
        name: String,
        mobile: String,
        date: String,
        filePart: File? = null
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true


                val response = repository.Createuser(token,name, mobile, date, filePart)

                if (response.isSuccessful) {

                    userRegisterResponse.postValue(response.body())

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




    fun hitUpdateUser(
        token: String,
        userId: String,
        name: String,
        mobile: String,
        date: String,
        filePart: File? = null
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true


                val response = repository.updateUserDetails(token,userId,name, mobile,
                    date, filePart)

                if (response.isSuccessful) {

                    userRegisterUpdateResponse.postValue(response.body())

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

    fun hitUserImage(
        token: String,
        userId: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true


                val response = repository.Userimages(token,userId)

                if (response.isSuccessful) {

                    UserImageResponseResponse.postValue(response.body())

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