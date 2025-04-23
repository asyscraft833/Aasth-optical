package com.teen.videoplayer.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.R
import java.text.SimpleDateFormat
import java.util.Locale

class FullImagePagerAdapter(
    private val context: Context,
    private val imageUrls: List<data>
) : RecyclerView.Adapter<FullImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imgFullItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_full_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(imageUrls[position].file)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.imageView)
    }


}
