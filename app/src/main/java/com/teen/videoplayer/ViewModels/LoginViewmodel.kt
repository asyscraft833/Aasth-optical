package com.teen.videoplayer.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teen.videoplayer.Model.LoginResponse
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
class LoginViewmodel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var errorlogin = MutableLiveData<Throwable>()
    var progresslogin = MutableLiveData<Boolean>()
    val registerResponse = MutableLiveData<RegisterResponse>()
    val loginResponse = MutableLiveData<LoginResponse>()


    fun hitLogin(
        mobile: String,
        password: String,
        mpin: String,
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true


                val response = repository.Loginuser( mobile, password,mpin)

                if (response.isSuccessful) {

                    loginResponse.postValue(response.body())

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
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun hitRegister(
        name: String,
        mobile: String,
        password: String,
        filePart: File? = null
    ) {
        viewModelScope.launch {
            try {
                // Show progress
                progresslogin.value = true


//                val response = repository.registeruser(name, mobile, password,
//                    getCurrentDateTime(), filePart)
//
//                if (response.isSuccessful) {
//
//                    registerResponse.postValue(response.body())
//
//                }

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