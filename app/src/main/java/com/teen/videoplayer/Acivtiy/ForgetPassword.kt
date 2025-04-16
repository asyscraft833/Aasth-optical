package com.teen.videoplayer.Acivtiy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.ViewModels.LoginViewmodel
import com.teen.videoplayer.databinding.ActivityForgetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgetPassword : BaseActivity() {

    private val viewmodel: LoginViewmodel by viewModels()
    lateinit var binding : ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)


        binding.forgetpassword.setOnClickListener {
            if (Validation() ) {
                val intent = Intent(this,ChangePasswordScreen::class.java)
                startActivity(intent)
            }

        }

    }

    fun Validation():Boolean{

        if (binding.etmobile.text.toString().isEmpty()){
            toast(this,"Enter your mobile number")
            return false

        }
        return true
    }
}