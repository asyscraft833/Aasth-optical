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
import com.teen.videoplayer.databinding.ActivityChangePasswordScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordScreen : BaseActivity() {

    private val viewmodel: LoginViewmodel by viewModels()

    lateinit var binding : ActivityChangePasswordScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_screen)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password_screen)

        binding.changepasswordbtn.setOnClickListener {
            if (Validation() ) {
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }


        }

    }
    fun Validation():Boolean{

        if (binding.etpassword.text.toString().isEmpty()){
            toast(this,"Enter your new password")
            return false
        }else if (binding.etpassword.text.toString().isEmpty()){
            toast(this,"Re-Enter your new password")
            return false
        }
        else if (binding.etpassword.text.toString().equals(binding.etpasswordreEnter.text.toString())){
            toast(this,"password not match")
            return false
        }
        return true
    }
}