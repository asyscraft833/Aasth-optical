package com.teen.videoplayer.Acivtiy

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.teen.videoplayer.Adapters.AllImageAdapter
import com.teen.videoplayer.Adapters.ImageAdapter
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.Model.dataAll
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.ImageViewerUtils.showUserDeleteAlertBox
import com.teen.videoplayer.Utils.ImageViewerUtils.showUserDeleteAlertBoxImage
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.ViewModels.DashBoardViewmodel
import com.teen.videoplayer.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : BaseActivity() {

    lateinit var binding : ActivityGalleryBinding

    private val viewmodel: DashBoardViewmodel by viewModels()
    lateinit var adapter : AllImageAdapter


    val dataList = mutableListOf<dataAll>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        binding.backbtn.setOnClickListener { finish() }

        UserAllImage()
        observer()
        SetupRecyclerview()


    }

    private fun SetupRecyclerview() {

        val layoutManager = GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false)
        binding.imageRecyclerview.layoutManager = layoutManager

        adapter = AllImageAdapter(this,dataList, onItemClick = { imageId,position ->

            showUserDeleteAlertBoxImage(this) { confirmed ->
                if (confirmed) {
                    dataList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    DeleteUserImage(imageId)
                }
            }

        })

        binding.imageRecyclerview.adapter = adapter

    }


    fun DeleteUserImage(id: String){
        if (NetworkUtils.isInternetAvailable(this)){
            val token = "Bearer "+userPref.getToken().toString()
            viewmodel.deleteUserImages(token,id)

        }else{
            toast(this,"Please check your Internet Connection")
        }

    }


    private fun observer() {
        viewmodel.GetAllIMagesResponseList.observe(this) { response ->
            dataList.clear()
            dataList.addAll(response.data)
            binding.totalitem.text = dataList.size.toString()
            adapter.updateList(dataList)
        }


        viewmodel.deleteUserResponse.observe(this) {
            toast(this,it.message.toString())
            binding.totalitem.text = dataList.size.toString()
//            UserAllImage()

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

    fun UserAllImage() {
        if (NetworkUtils.isInternetAvailable(this)) {
            val token = "Bearer " + userPref.getToken().toString()

            viewmodel.hitGalleryImage(token)


        } else {
            toast(this, "Please check your Internet Connection")
        }


    }
}