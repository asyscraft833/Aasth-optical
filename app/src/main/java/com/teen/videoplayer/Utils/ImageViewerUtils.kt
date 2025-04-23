package com.teen.videoplayer.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.viewpager2.widget.ViewPager2
import com.teen.videoplayer.Adapters.FullImagePagerAdapter
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.R
import java.text.SimpleDateFormat
import java.util.Locale


object ImageViewerUtils {

    fun fullImageview(context: Context, imageList: List<data>, startPosition: Int = 0) {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_full_image, null)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.imageViewPager)
        val crossbutton = dialogView.findViewById<CardView>(R.id.crossbutton)
        val maxlayout = dialogView.findViewById<CardView>(R.id.maxlayout)
        val imgToggleSize = dialogView.findViewById<ImageView>(R.id.imgToggleSize)

        var isFullScreen = false

        val imageDateTextView = dialogView.findViewById<TextView>(R.id.imageuploaddate)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val rawDate = imageList[position].date
                val formattedDate = convertDateFormat(rawDate)
                imageDateTextView.text = formattedDate
            }
        })


        val adapter = FullImagePagerAdapter(context, imageList)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(startPosition, false)

        val dialog = Dialog(context)
        dialog.setContentView(dialogView)
        dialog.window?.setLayout(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350f, context.resources.displayMetrics).toInt(),
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, context.resources.displayMetrics).toInt()
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)


        val defaultWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350f, context.resources.displayMetrics).toInt()
        val defaultHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, context.resources.displayMetrics).toInt()



        maxlayout.setOnClickListener {
            if (isFullScreen) {
                // Minimize
                dialog.window?.setLayout(defaultWidth, defaultHeight)
                imgToggleSize.setImageResource(R.drawable.open_in_full_24px)
                isFullScreen = false
            } else {
                // Maximize
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                imgToggleSize.setImageResource(R.drawable.close_fullscreen_24px)
                isFullScreen = true
            }
        }

        dialogView.setOnClickListener {
            dialog.dismiss()
        }
        crossbutton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showUserDeleteAlertBox(context: Context, onResult: (Boolean) -> Unit) {
        AlertDialog.Builder(context, R.style.CustomAlertDialog)
            .setTitle("Delete")
            .setMessage("Do you want to delete this User?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                onResult(true)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                onResult(false) // User canceled
            }
            .show()
    }



    private fun convertDateFormat(inputDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            inputDate // fallback if parsing fails
        }
    }

}