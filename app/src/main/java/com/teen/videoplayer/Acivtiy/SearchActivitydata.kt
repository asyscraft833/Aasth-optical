package com.teen.videoplayer.Acivtiy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.teen.videoplayer.Adapters.dashboardAdapter
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.DashBoardViewmodel
import com.teen.videoplayer.databinding.ActivityMainBinding
import com.teen.videoplayer.databinding.ActivitySearchActivitydataBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Query

@AndroidEntryPoint
class SearchActivitydata : BaseActivity() {

    lateinit var binding: ActivitySearchActivitydataBinding
    private val viewmodel: DashBoardViewmodel by viewModels()

    var datalist = mutableListOf<UserDetails>()
    lateinit var adapter :dashboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_activitydata)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_activitydata)

        binding.backbtn.setOnClickListener { finish() }

        Searchdata()
        observer()
        SetupRecylerview()

    }

    private fun SetupRecylerview(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = dashboardAdapter(this, emptyList(), onItemClick = { position, flag ->

            if (flag==1){
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("userDetails", datalist[position])
                intent.putExtra("flag", flag)
                startActivity(intent)
            }else if (flag==3){
                val intent = Intent(this, AddUserActivity::class.java)
                intent.putExtra("userDetails", datalist[position])
                intent.putExtra("flag", flag)
                startActivity(intent)
            }
            else if(flag==2){

                datalist.removeAt(position)
                adapter.notifyDataSetChanged()
                DeletuserEntry(datalist[position].id.toString())

            }

        })
        binding.recyclerView.adapter = adapter
    }


    private fun observer() {

        viewmodel.dashBoardResponseFilter.observe(this) { response ->
            response?.let {
                datalist.clear()
                datalist.addAll(it.users)
                adapter.updateList(datalist)

                if (datalist.isEmpty()){
                    binding.notFoundview.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }else
                {
                    binding.notFoundview.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }

        viewmodel.deleteUserResponse.observe(this) { response ->
            toast(this,response.message.toString())

        }


        viewmodel.errorlogin.observe(this) { errorMessage ->
            Log.e("API", errorMessage.message.toString())
        }
    }



    fun UserdetailAPiFilter(query: String){
        if (NetworkUtils.isInternetAvailable(this)){
            val token = "Bearer "+userPref.getToken().toString()
            viewmodel.hitDashBoardFilter(token,query)

        }else{
            toast(this,"Please check your Internet Connection")
        }

    }


    fun DeletuserEntry(userid : String){
        if (NetworkUtils.isInternetAvailable(this)){
            val token = "Bearer "+userPref.getToken().toString()
            viewmodel.deleteUserDetails(token,userid)

        }else{
            toast(this,"Please check your Internet Connection")
        }


    }


    fun Searchdata(){
        binding.searchdata.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                UserdetailAPiFilter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Optional - do something after text changes
            }
        })

    }

}