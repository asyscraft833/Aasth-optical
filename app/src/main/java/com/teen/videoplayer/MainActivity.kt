package com.teen.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.teen.videoplayer.Acivtiy.AddUserActivity
import com.teen.videoplayer.Acivtiy.GalleryActivity
import com.teen.videoplayer.Acivtiy.LoginActivity
import com.teen.videoplayer.Acivtiy.MonthlyReportActivity
import com.teen.videoplayer.Acivtiy.SearchActivitydata
import com.teen.videoplayer.Acivtiy.TotalCustomerActivity
import com.teen.videoplayer.Adapters.MenuAdapter
import com.teen.videoplayer.Adapters.dashboardAdapter
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.DashBoardViewmodel
import com.teen.videoplayer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

data class MenuItem(val iconResId: Int, val text: String)

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewmodel: DashBoardViewmodel by viewModels()
    lateinit var adapter: dashboardAdapter

    var datalist = mutableListOf<UserDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.addbtn.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }

        binding.monthlycard.setOnClickListener {
            val intent = Intent(this, MonthlyReportActivity::class.java)
            startActivity(intent)
        }

        binding.totalimage.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }


        binding.totalcustomer.setOnClickListener {
            val intent = Intent(this, TotalCustomerActivity::class.java)
            startActivity(intent)
        }


        binding.searchIcon.setOnClickListener {
            startActivity(Intent(this, SearchActivitydata::class.java))
        }

        binding.menuIcon.setOnClickListener {
            toggleDrawer()
        }

        binding.logout.setOnClickListener {
            userPref.clearPref()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


        UserdetailAPi()
        observer()
        SetupRecylerview()
        SetSidemenuList()


    }


    fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun SetSidemenuList() {
        val menuItems = listOf(
            MenuItem(R.drawable.baseline_person_243, "My Profile"),
            MenuItem(R.drawable.statistics_icon, "Statistics"),
            MenuItem(R.drawable.baseline_settings_24, "Settings"),
            MenuItem(R.drawable.upload_24px, "Export"),
            MenuItem(R.drawable.publish_24px, "Import"),
            MenuItem(R.drawable.baseline_logout_24, "Logout"),
        )

        val adapter = MenuAdapter(menuItems) { menuItem -> DrawerClickHandle(menuItem) }
        binding.menuRecyckerview.adapter = adapter

    }


    fun DrawerClickHandle(menuItem: MenuItem) {


        when (menuItem.text) {
            "My Profile" -> {
                toast(this, "Coming Soon")

            }

            "Settings" -> {

                toast(this, "Coming Soon")
            }

            "Export" -> {

                toast(this, "Coming Soon")
            }

            "Import" -> {

                toast(this, "Coming Soon")
            }

            "Statistics" -> {

                val intent = Intent(this, MonthlyReportActivity::class.java)
                startActivity(intent)
            }

            "Logout" -> {
                performLogout()
            }
        }
        binding.drawerLayout.closeDrawers()
    }

    private fun performLogout() {

        toast(this, "Logged out successfully")

        userPref.isLogin = false
        userPref.clearPref()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onResume() {
        super.onResume()
        UserdetailAPi()
    }

    private fun SetupRecylerview() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = dashboardAdapter(this, emptyList(), onItemClick = { position, flag ->

            if (flag == 1) {
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("flag", flag)
                intent.putExtra("UserId", datalist[position].id.toString())
                intent.putExtra("name", datalist[position].name)
                intent.putExtra("number", datalist[position].phone)
                intent.putExtra("imageurl", datalist[position].file)
                intent.putExtra("date", datalist[position].created_at)
                startActivity(intent)
            } else if (flag == 3) {
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("flag", flag)
                intent.putExtra("UserId", datalist[position].id.toString())
                intent.putExtra("name", datalist[position].name)
                intent.putExtra("number", datalist[position].phone)
                intent.putExtra("imageurl", datalist[position].file)
                intent.putExtra("date", datalist[position].created_at)
                startActivity(intent)
            } else if (flag == 2) {
                dialogfun(datalist[position].id.toString())

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


    fun UserdetailAPi() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()
            viewmodel.hitDashBoard(token)

        } else {
            toast(this, "Please check your Internet Connection")
        }


    }

    fun DeletuserEntry(userid: String) {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()
            viewmodel.deleteUserDetails(token, userid)

        } else {
            toast(this, "Please check your Internet Connection")
        }


    }

    private fun observer() {

        viewmodel.dashBoardResponse.observe(this) { response ->
            response?.let {
                datalist.clear()
                datalist.addAll(it.user)
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