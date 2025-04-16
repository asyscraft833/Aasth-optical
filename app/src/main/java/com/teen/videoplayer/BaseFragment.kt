package com.teen.videoplayer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.teen.videoplayer.Utils.UserPref
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
open class BaseFragment : Fragment() {

    @Inject
    lateinit var userPref: UserPref


    var dialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        userPref=UserPref(requireContext())


    }

    @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
    protected fun showProgressDialog() {
        if (dialog == null)
            dialog = Dialog(requireContext())
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



    fun changeLanguage(prefLanguage: String) {
        val locale = Locale(prefLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun snackbar(message: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }




}