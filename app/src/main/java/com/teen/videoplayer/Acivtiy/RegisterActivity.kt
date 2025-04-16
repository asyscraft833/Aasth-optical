package com.teen.videoplayer.Acivtiy

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.LoginViewmodel
import com.teen.videoplayer.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.io.File


@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    private val viewmodel: LoginViewmodel by viewModels()

    lateinit var binding:ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.registerbutton.setOnClickListener {
            if(Validation()) HitRegisterApi()
            
        }

        Obervers()

    }

    fun Validation():Boolean{
        if (binding.etname.text.toString().isEmpty()){
            toast(this,"Enter your name")
            return false

        }else if (binding.etmobile.text.toString().isEmpty()){
            toast(this,"Enter your mobile number")
            return false

        }else if (binding.etpassword.text.toString().isEmpty()){
            toast(this,"Enter your password")
            return false

        }
        return true
    }



    private fun Obervers(){
        
        viewmodel.errorlogin.observe(this){
            Log.e("error","Error : "+it.message.toString())
        }

        viewmodel.progresslogin.observe(this){
            if (it) {
                showProgressDialog()
            }else{
                hideProgressDialog()
            }
        }

        viewmodel.registerResponse.observe(this){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        
    }


    private fun getDrawableAsFile(context: Context, drawableId: Int): File {
        val drawable = ContextCompat.getDrawable(context, drawableId) as BitmapDrawable
        val bitmap = drawable.bitmap

        val file = File(context.cacheDir, "placeholder.jpg") // Temporary file
        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }

        return file
    }



    fun HitRegisterApi(){
        if (NetworkUtils.isInternetAvailable(this)){

            val file = getDrawableAsFile(this, R.drawable.image)

            viewmodel.hitRegister(
                binding.etname.text.toString(),
                binding.etmobile.text.toString(),
                binding.etpassword.text.toString(),
                file

            )
        }
        else{
            toast(this,"Please check Internet Connection")
        }

    }
}