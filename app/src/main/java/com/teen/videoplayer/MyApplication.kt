package com.teen.videoplayer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.teen.videoplayer.Utils.UserPref
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {


    @Inject
    lateinit var userPref: UserPref

    override fun onCreate() {
        super.onCreate()
        userPref=UserPref(this)

        AppCompatDelegate.setDefaultNightMode(
            if (userPref.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}