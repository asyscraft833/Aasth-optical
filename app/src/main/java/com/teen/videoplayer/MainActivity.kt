package com.teen.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
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
import com.teen.videoplayer.Utils.ImageViewerUtils.showUserDeleteAlertBox
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
            intent.putExtra("flag", true)
            startActivity(intent)
        }

        binding.newweekdatacard.setOnClickListener {
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

        binding.logoutbtn.setOnClickListener {

            showLogoutDialog()

        }


        UserdetailAPi()
        observer()
        SetupRecylerview()
//        SetSidemenuList()
        HandleMode()


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

            MenuItem(R.drawable.baseline_logout_24, "Logout"),
        )

        val adapter = MenuAdapter(menuItems) { menuItem -> DrawerClickHandle(menuItem) }
        binding.menuRecyckerview.adapter = adapter

    }

    private fun HandleMode() {

        if (userPref.isDarkMode) binding.modeSwitchCase.isChecked =
            true else binding.modeSwitchCase.isChecked = false



        binding.modeSwitchCase.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                userPref.isDarkMode = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                userPref.isDarkMode = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }


    fun DrawerClickHandle(menuItem: MenuItem) {


        when (menuItem.text) {
            "Logout" -> {
                performLogout()
            }
        }
        binding.drawerLayout.closeDrawers()
    }

    private fun showLogoutDialog() {
        // Create the AlertDialog builder
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)

        // Set the message for the dialog
        builder.setMessage("Are you sure you want to logout?")
            .setCancelable(false)  // Disable touch outside to dismiss
            .setPositiveButton("Yes") { dialog, id ->
                // Handle logout here
                performLogout()
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog when the user clicks No
                dialog.dismiss()
            }

        // Create and show the dialog
        val alert = builder.create()
        alert.show()
    }

    private fun performLogout() {

        toast(this, "Logged out successfully")

        userPref.isLogin = false
        userPref.clearPref()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


//    override fun onResume() {
//        super.onResume()
//        UserdetailAPi()
//    }

    private fun SetupRecylerview() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = dashboardAdapter(this, emptyList(), onItemClick = { position, flag ->

            if (flag == 1) {
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("userDetails", datalist[position])
                intent.putExtra("flag", flag)
                startActivity(intent)

            } else if (flag == 3) {
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


            } else {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$flag")
                }
                startActivity(dialIntent)

            }
        })
        binding.recyclerView.adapter = adapter
    }


    fun UserdetailAPi() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()

            viewmodel.hitDashBoard(token)
            viewmodel.HitDashboardCount(token)


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


        viewmodel.dashBoardResponsecounts.observe(this) { response ->
            response?.let {

                binding.totalcustomercount.text = it.totaluserscount.toString() + " Users"
                binding.weekusercount.text = it.weeklyuserscount.toString() + " Users"
                binding.monthusercount.text = it.monthlyuserscount.toString() + " New users"
                binding.totalimagecount.text = it.totalimagescount.toString() + " Images"
            }
        }

        viewmodel.deleteUserResponse.observe(this) { response ->
            toast(this, response.message.toString())
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