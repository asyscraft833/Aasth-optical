package com.teen.videoplayer.Utils

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.teen.videoplayer.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit

object CameraUtils {

    private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private var currentPhotoPath: String = ""

    // ✅ Function to check if camera permission is granted
    fun checkPermissionForCamera(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // ✅ Function to request camera permission
    fun requestPermissionForCamera(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    // ✅ Function to open the camera
    fun openCamera(context: Context, launcher: ActivityResultLauncher<Intent>) {
        if (checkPermissionForCamera(context)) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile(context)
            } catch (ex: IOException) {
                Log.e("Camera", "Error creating file", ex)
                null
            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcher.launch(takePictureIntent)
            }
        } else {
            requestPermissionForCamera(context as Activity)
        }
    }

    // ✅ Function to create an image file
    private fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDir!!).apply {
            currentPhotoPath = absolutePath
        }
    }

    // ✅ Get the current photo path
    fun getCurrentPhotoPath(): String {
        return currentPhotoPath
    }



    // ✅ Function to open the gallery
    fun openGallery(launcher: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(galleryIntent)
    }





    // ✅ Function to convert Bitmap to File
    fun bitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = createImageFile(context)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    // ✅ Function to show a date picker
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            R.style.CustomDatePickerDialog,
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Callback function with selected date
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set the max date to today
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis

        // Show the date picker dialog
        datePickerDialog.show()
    }

}
