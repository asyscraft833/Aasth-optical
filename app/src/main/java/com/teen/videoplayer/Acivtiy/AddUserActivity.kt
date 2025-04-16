package com.teen.videoplayer.Acivtiy

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.teen.videoplayer.Adapters.ImageAdapter
import com.teen.videoplayer.BaseActivity
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.CameraUtils
import com.teen.videoplayer.Utils.CameraUtils.openCamera
import com.teen.videoplayer.Utils.CameraUtils.openGallery
import com.teen.videoplayer.Utils.NetworkUtils
import com.teen.videoplayer.Utils.PermissionUtils
import com.teen.videoplayer.ViewModels.AddUserViewmodel
import com.teen.videoplayer.databinding.ActivityAddUserBinding
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URL
import java.util.Calendar

@AndroidEntryPoint
class AddUserActivity : BaseActivity() {

    private val viewmodel: AddUserViewmodel by viewModels()
    private lateinit var binding: ActivityAddUserBinding
    private var userid = ""
    private var flag  = 0
    private var selectimage = false

    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cropLauncher: ActivityResultLauncher<Intent>

    lateinit var adapter : ImageAdapter

    private var imageFile: File? = null
    private var imageurl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)

        flag = intent.getIntExtra("flag",0)

        if (flag==1  || flag==3) {
            binding.registerbutton.text = "Update"
            userid = intent.getStringExtra("UserId").toString()
            binding.etname.setText(intent.getStringExtra("name"))
            binding.etmobile.setText(intent.getStringExtra("number"))
            binding.etDate.setText(intent.getStringExtra("date"))
            imageurl = intent.getStringExtra("imageurl").toString()
            Log.d("updatedata","Userid  : "+userid)

            HitUserImages(userid)
        }

        if (flag==3)  {
            binding.registerbutton.visibility = View.GONE
            binding.uploadimagelayout.visibility = View.GONE
            binding.uploadimagelayouttext.visibility = View.GONE
            binding.localview.visibility = View.GONE
            binding.etname.isEnabled = false
            binding.etmobile.isEnabled = false
            binding.etDate.isEnabled = false
            binding.titletext.text = "User Info"


        } else if(flag==1){
            binding.titletext.text = "Update detail"
            binding.uploadimagelayout.visibility = View.VISIBLE
            binding.uploadimagelayouttext.visibility = View.VISIBLE
            binding.registerbutton.visibility = View.VISIBLE
        }

        binding.registerbutton.setOnClickListener {
            if (flag==1) HitUserApiUpdate() else HitUserApi()
        }



        binding.etDate.setOnClickListener { showDatePicker() }
        binding.backbtn.setOnClickListener { finish() }



        binding.opencamera.setOnClickListener {
            checkpermission(true)
        }



        binding.opengallery.setOnClickListener {
            checkpermission(false)
        }

        val layoutManager = GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false)
        binding.imageRecyclerview.layoutManager = layoutManager

       adapter = ImageAdapter(this,emptyList(), onItemClick = { position, flag ->



        })


        binding.imageRecyclerview.adapter = adapter


        observeViewModel()
        RecieveCamera()
    }


    private fun RecieveCamera() {
        // Camera result launcher
        cameraResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val photoPath = CameraUtils.getCurrentPhotoPath()
                    val photoFile = File(photoPath)
                    if (photoFile.exists()) {
                        startCrop(Uri.fromFile(photoFile))
                    }

                }
            }

        // Gallery result launcher
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri? = result.data?.data

                    if (imageUri != null) {
                        startCrop(imageUri)
                    }

                }
            }

        cropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val resultUri = UCrop.getOutput(result.data!!)

                if (resultUri != null) {
                    binding.imageview.setImageURI(resultUri)

                    try {
                        // Copy URI content to a temporary file in cache directory
                        val inputStream = contentResolver.openInputStream(resultUri)
                        val tempFile = File.createTempFile("cropped_", ".jpg", cacheDir)
                        inputStream?.use { input ->
                            tempFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }

                        imageFile = tempFile.absoluteFile

                        Log.d("CropResult", "Image file saved at: ${imageFile}")

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to process cropped image", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Cropping failed", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image_${System.currentTimeMillis()}.jpg"))

        val options = UCrop.Options().apply {
            setCompressionQuality(80)
            setFreeStyleCropEnabled(true)
            setHideBottomControls(true)
            setToolbarColor(ContextCompat.getColor(this@AddUserActivity, R.color.primarycolor))
            setStatusBarColor(ContextCompat.getColor(this@AddUserActivity, R.color.primarycolor))
            setActiveControlsWidgetColor(ContextCompat.getColor(this@AddUserActivity, R.color.primarycolor))
        }

        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)

        cropLauncher.launch(uCrop.getIntent(this))
    }



    private fun checkpermission(type: Boolean) {
        // Check camera permission
        if (PermissionUtils.checkPermission(this, Manifest.permission.CAMERA)) {
            if (type) {
                selectimage = true
                openCamera(this, cameraResultLauncher)
            } else {
                selectimage = true
                openGallery(galleryLauncher)
            }

        } else {
            // Permission not granted, request permission
            PermissionUtils.requestPermission(
                this,
                Manifest.permission.CAMERA,
                PermissionUtils.CAMERA_REQUEST_CODE
            )
        }
    }

    private fun getDrawableAsFile(context: Context, drawableId: Int): File {
        val drawable = ContextCompat.getDrawable(context, drawableId) as BitmapDrawable
        val bitmap = drawable.bitmap
        val file = File(context.cacheDir, "placeholder.jpg")
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        return file
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val date = String.format("%04d-%02d-%02d", year, month + 1, day)
                binding.etDate.setText(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun HitUserApi() {
        if (!NetworkUtils.isInternetAvailable(this)) {
            toast(this, "Please check Internet Connection")
            return
        }
        if (Validation()) {
            val token = "Bearer ${userPref.getToken()}"

            val file = imageFile?.let { it } ?: getDrawableAsFile(this, R.drawable.image)

            viewmodel.hitCreateUser(
                token,
                binding.etname.text.toString(),
                binding.etmobile.text.toString(),
                binding.etDate.text.toString(),
                file
            )
        }
    }




    private fun HitUserApiUpdate() {
        if (!NetworkUtils.isInternetAvailable(this)) {
            toast(this, "Please check Internet Connection")
            return
        }

        if (Validation()) {
            val token = "Bearer ${userPref.getToken()}"

                // 1. User ne naya image select kiya
            val fileToUpload = if (selectimage && imageFile != null) {
                compressImageFile(imageFile!!)
            } else {
                null // ya koi default File ya empty string based on use case
            }

//            val fileToUpload = imageFile?.let { it } ?: ""

            Log.d("updatedata", "Token : $token")
            Log.d("updatedata", "Userid  : $userid")
            Log.d("updatedata", "Name : ${binding.etname.text}")
            Log.d("updatedata", "Mobile : ${binding.etmobile.text}")
            Log.d("updatedata", "File : $fileToUpload")

            viewmodel.hitUpdateUser(
                token,
                userid,
                binding.etname.text.toString(),
                binding.etmobile.text.toString(),
                binding.etDate.text.toString(),
                fileToUpload
            )
        }

    }

    private fun HitUserImages(Userid : String) {
        if (!NetworkUtils.isInternetAvailable(this)) {
            toast(this, "Please check Internet Connection")
            return
        }


        val token = "Bearer ${userPref.getToken()}"
        viewmodel.hitUserImage(
            token,
            Userid
        )

    }

    private fun compressImageFile(originalFile: File): File {
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)
        val compressedFile = File(cacheDir, "compressed_image_upload.jpg")
        compressedFile.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        }
        return compressedFile
    }

    private fun Validation(): Boolean {
        return when {
            binding.etname.text.toString().isEmpty() -> {
                toast(this, "Enter your name"); false
            }

            binding.etmobile.text.toString().isEmpty() -> {
                toast(this, "Enter your mobile number"); false
            }
            binding.etmobile.text.toString().length<10 -> {
                toast(this, "Enter your valid number"); false
            }

            binding.etDate.text.toString().isEmpty() -> {
                toast(this, "Enter the date"); false
            }

            else -> true
        }
    }

    private fun observeViewModel() {
        viewmodel.errorlogin.observe(this) {
            toast(this, it.message ?: "Unknown error")
        }
        viewmodel.progresslogin.observe(this) {
            if (it) showProgressDialog() else hideProgressDialog()
        }


        viewmodel.UserImageResponseResponse.observe(this) {

            adapter.updateList(it.data)


        }

        viewmodel.userRegisterResponse.observe(this) {
             finish()
        }

        viewmodel.userRegisterUpdateResponse.observe(this) {
            if (it.success) finish()
        }
    }

}
