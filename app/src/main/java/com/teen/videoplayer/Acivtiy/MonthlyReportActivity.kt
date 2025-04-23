package com.teen.videoplayer.Acivtiy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.teen.videoplayer.Adapters.MonthlyReportAdapter
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.ImageViewerUtils.showUserDeleteAlertBox
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.DashBoardViewmodel
import com.teen.videoplayer.databinding.ActivityMonthlyReportBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthlyReportActivity : BaseActivity() {
    lateinit var binding: ActivityMonthlyReportBinding

    private val viewmodel: DashBoardViewmodel by viewModels()
    lateinit var adapter: MonthlyReportAdapter

    var datalist = mutableListOf<UserDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_report)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_monthly_report)

        binding.backbtn.setOnClickListener { finish() }

        val flag = intent.getBooleanExtra("flag", false)

        if (flag) {
            binding.titletv.text = "Monthly Report"
            UserdetailAPi()
        } else {
            binding.titletv.text = "Weekly Report"
            UserWeeklyData()
        }


        observer()
        SetupRecylerview()
        setupSearchFilter()

    }

    private fun setupSearchFilter() {
        binding.searchdata.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                val filteredCategories = if (query.isEmpty()) {
                    datalist
                } else {
                    datalist.filter { it.name != null }
                        .filter { it.name!!.contains(query, ignoreCase = true) }


                }
                adapter.updateList(filteredCategories.toMutableList())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun SetupRecylerview() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MonthlyReportAdapter(this, emptyList(), onItemClick = { position, flag ->

            if (flag == 1) {


                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("userDetails", datalist[position])
                intent.putExtra("flag", flag)
                startActivity(intent)

            } else if (flag == 2) {


                showUserDeleteAlertBox(this) { confirmed ->
                    if (confirmed) {
                        datalist.removeAt(position)
                        adapter.notifyDataSetChanged()
                        DeletuserEntry(datalist[position].id.toString())
                    }
                }

            } else if (flag == 3) {

                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("flag", flag)
                intent.putExtra("userDetails", datalist[position])
                startActivity(intent)

            } else {

                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$flag")
                }
                startActivity(dialIntent)

            }
        })

        binding.recyclerView.adapter = adapter
    }

    fun dialogfun(userId: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this User?")
            .setPositiveButton("Yes") { dialog, _ ->
                DeletuserEntry(userId)
                Toast.makeText(this, "user deleted Successfuly", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    fun DeletuserEntry(userid: String) {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()
            viewmodel.deleteUserDetails(token, userid)

        } else {
            toast(this, "Please check your Internet Connection")
        }


    }

    fun UserdetailAPi() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()
            viewmodel.Hitmonthlydata(token)

        } else {
            toast(this, "Please check your Internet Connection")
        }

    }

    fun UserWeeklyData() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()
            viewmodel.Hitweeklydata(token)

        } else {
            toast(this, "Please check your Internet Connection")
        }

    }


    private fun observer() {

        viewmodel.monthlyReportreponse.observe(this) { response ->
            response?.let {
                datalist.clear()
                datalist.addAll(it.users)
                adapter.updateList(datalist)
                binding.totalitem.text = datalist.count().toString()
            }
        }

        viewmodel.deleteUserResponse.observe(this) { response ->
            UserdetailAPi()
        }

        viewmodel.progresslogin.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewmodel.errorlogin.observe(this) { errorMessage ->
            Log.e("API", errorMessage.message.toString())
        }
    }
}