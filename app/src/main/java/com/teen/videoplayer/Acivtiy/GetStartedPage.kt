package com.teen.videoplayer.Acivtiy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.MainActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.databinding.ActivityGetStartedPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GetStartedPage : BaseActivity() {

    lateinit var binding: ActivityGetStartedPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_started_page)

        binding.startbtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(100)
            if (userPref.isLogin) {
                startActivity(Intent(this@GetStartedPage, MainActivity::class.java))
                finish()
            }
        }
    }
}