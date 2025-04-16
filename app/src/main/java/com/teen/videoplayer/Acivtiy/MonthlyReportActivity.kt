package com.teen.videoplayer.Acivtiy

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.databinding.ActivityMonthlyReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyReportActivity : BaseActivity() {
    lateinit var binding : ActivityMonthlyReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_report)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monthly_report)

    }
}