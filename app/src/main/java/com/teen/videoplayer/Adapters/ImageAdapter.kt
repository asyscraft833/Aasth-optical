package com.teen.videoplayer.Adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.Model.UserImageResponse
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.R
import com.teen.videoplayer.databinding.DashbardRowLayoutBinding
import com.teen.videoplayer.databinding.ImageRowItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageAdapter(
    private val context: Context,
    private var items: List<data>,
    private val onItemClick: (Int,Int) -> Unit

) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    // ViewHolder with ViewBinding
    class ViewHolder(val binding: ImageRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener {
            onItemClick(position,3)
        }


        holder.binding.apply {

            imageview.setOnClickListener {

                fullImageview(
                    imageList = items,
                    date = item.date,
                    startPosition = position
                )
            }



            Glide
                .with(context)
                .load(item.file)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(imageview)


        }
    }

    fun updateList(newlist : List<data>){
        items = newlist
        notifyDataSetChanged()
    }


    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

    fun fullImageview(imageList: List<data>, date: String, startPosition: Int = 0) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_full_image, null)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.imageViewPager)
        val imagedate = dialogView.findViewById<TextView>(R.id.imageuploaddate)
        val crossbutton = dialogView.findViewById<CardView>(R.id.crossbutton)
        val maxlayout = dialogView.findViewById<CardView>(R.id.maxlayout)
        val imgToggleSize = dialogView.findViewById<ImageView>(R.id.imgToggleSize)




        var isFullScreen = false


//        imagedate.text = convertDateFormat(date)

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
                imgToggleSize.setImageResource(R.drawable.close_fullscreen_24px)
                isFullScreen = false
            } else {
                // Maximize
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                imgToggleSize.setImageResource(R.drawable.open_in_full_24px)
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


}
