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
import android.view.View
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
import com.teen.videoplayer.Utils.ImageViewerUtils
import com.teen.videoplayer.databinding.DashbardRowLayoutBinding
import com.teen.videoplayer.databinding.ImageRowItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageAdapter(
    private val context: Context,
    private var items: MutableList<data>,
    private val flag : Int,
    private val onItemClick: (String,Int) -> Unit

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

//        holder.binding.imageview.setOnLongClickListener {
//            onItemClick(item.imageid.toString(),position)
//            true
//        }

        if (flag==1) holder.binding.deleteimage.visibility = View.VISIBLE

        holder.binding.deleteimage.setOnClickListener {
            onItemClick(item.imageid.toString(),position)
        }


        holder.binding.apply {

            imageview.isClickable = true
            imageview.setOnClickListener {
                ImageViewerUtils.fullImageview(context, items,startPosition = position)

            }


            Glide
                .with(context)
                .load(item.file)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(imageview)


        }
    }

    fun updateList(newlist : MutableList<data>){
        items = newlist
        notifyDataSetChanged()
    }


}
