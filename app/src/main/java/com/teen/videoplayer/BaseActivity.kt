package com.teen.videoplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import com.google.android.material.snackbar.Snackbar
import com.teen.videoplayer.Utils.UserPref

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var userPref: UserPref

    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
         userPref=UserPref(this)



        AppCompatDelegate.setDefaultNightMode(
            if (userPref.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )


        if (Build.MANUFACTURER.equals("Xiaomi", ignoreCase = true)) {
            setMIUIStatusBarDarkMode(this, darkMode = true)
        }

    }

    @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
    protected fun showProgressDialog() {
        if (dialog == null)
            dialog = Dialog(this)
        dialog!!.setContentView(R.layout.progress_dialog)
        dialog!!.setCancelable(false)
        //  dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(droidninja.filepicker.R.color.transparent_black));

        if (dialog != null && !dialog!!.isShowing)
            dialog!!.show()
    }
    protected fun hideProgressDialog() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog!!.isShowing)
            dialog!!.dismiss()
    }


    fun setMIUIStatusBarDarkMode(activity: Activity, darkMode: Boolean) {
        val window: Window = activity.window
        try {
            val clazz = window.javaClass
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            if (darkMode) {
                // Enable dark mode for status bar icons (for a light background)
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                // Disable dark mode for status bar icons (for a dark background)
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
        } catch (e: Exception) {
            // MIUI-specific flag might not be available on this device/version
            e.printStackTrace()
        }
    }



    fun changeLanguage(prefLanguage: String) {
        val locale = Locale(prefLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun snackbar(message: String) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}