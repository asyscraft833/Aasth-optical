package com.teen.videoplayer.Utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPref @Inject constructor(@ApplicationContext context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("userPref", Context.MODE_PRIVATE)


    var isLogin: Boolean
        get() = preferences.getBoolean("isLoginA", false)
        set(login) = preferences.edit().putBoolean("isLoginA", login).apply()


 var isDarkMode: Boolean
        get() = preferences.getBoolean("Mode", false)
        set(login) = preferences.edit().putBoolean("Mode", login).apply()


    fun clearPref() {
        preferences.edit().clear().apply()
    }

    fun setToken(token: String?) {
        preferences.edit().putString("token", token).apply()
    }

    fun getToken(): String? {
        return preferences.getString("token", null)
    }

    fun setUserId(Userid: String?) {
        preferences.edit().putString("UserId", Userid).apply()
    }

    fun getUserId(): String? {
        return preferences.getString("UserId", null)
    }

    fun setMobileNumber(mobile: String?) {
        preferences.edit().putString("mobile", mobile).apply()
    }

    fun getMobileNumber(): String? {
        return preferences.getString("mobile", null)
    }

    fun setName(name: String?) {
        preferences.edit().putString("name", name).apply()
    }

    fun getname(): String? {
        return preferences.getString("name", null)
    }

}