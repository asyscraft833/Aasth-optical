package com.teen.videoplayer.Utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    const val CAMERA_REQUEST_CODE = 1001
    const val MICROPHONE_REQUEST_CODE = 1001


    // Function to check permission
    fun checkPermission(context: Activity, permission: String): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED)
    }

    // Function to request permission
    fun requestPermission(context: Activity, permission: String, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            // You can show a rationale dialog here if needed before asking for permission
            Toast.makeText(context, "Permission required for camera access", Toast.LENGTH_SHORT)
                .show()
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode)
        }
    }

    // Function to handle permission result for Camera
    @RequiresApi(Build.VERSION_CODES.Q)
    fun handlePermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        context: Activity,
        onPermissionGranted: () -> Unit
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, call the passed function
                    onPermissionGranted()
                } else {
                    Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            MICROPHONE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, call the passed function
                    onPermissionGranted()
                } else {
                    Toast.makeText(context, "Audio record permission denied", Toast.LENGTH_SHORT).show()
                }
            }

            // You can add more cases for other permissions like storage, location, etc.
        }
    }



}

