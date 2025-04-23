package com.teen.videoplayer.Acivtiy

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.intuit.sdp.BuildConfig
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.MainActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.LoginViewmodel
import com.teen.videoplayer.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewmodel: LoginViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginbtn.setOnClickListener {

            if (validateInput()) {
                loginApi()
            }

        }

        observeViewModel()
        handleRegisterButton()

        binding.forgetpassword.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    override fun onStart() {
        super.onStart()

        if (userPref.isLogin) {
            lifecycleScope.launch {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun validateInput(): Boolean {
        when {
            binding.etmobile.text.toString().isEmpty() -> {
                toast(this, "Enter your mobile number")
                return false
            }
            binding.etpassword.text.toString().isEmpty() -> {
                toast(this, "Enter your password")
                return false
            }
            else -> return true
        }
    }

    private fun handleRegisterButton() {
        val fullText = "Don’t have an account? Register"
        val spannableString = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }

        val startIndex = fullText.indexOf("Register")
        val endIndex = startIndex + "Register".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvRegister.text = spannableString
    }



    private fun observeViewModel() {
        viewmodel.loginResponse.observe(this@LoginActivity) { response ->
            if (response.success) {
                lifecycleScope.launch(Dispatchers.IO) {
                    userPref.apply {
                        isLogin = response.success
                        setToken(response.access_token.toString())
                        setUserId(response.total_user.toString())
                        setMobileNumber(response.mobile.toString())
                        // setName(response.user.name.toString())
                    }

                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }


        viewmodel.errorlogin.observe(this@LoginActivity) { error ->
            Log.e("Error", "Error: ${error.message}")
        }

        viewmodel.progresslogin.observe(this@LoginActivity) { isLoading ->
            if (isLoading) showProgressDialog() else hideProgressDialog()
        }
    }

    private fun loginApi() {
        if (NetworkUtils.isInternetAvailable(this)) {

            viewmodel.hitLogin(
                binding.etmobile.text.toString(),
                binding.etpassword.text.toString(),
                binding.etmpin.text.toString()
            )

        } else {
            toast(this, "Please check your Internet Connection")
        }
    }
}
