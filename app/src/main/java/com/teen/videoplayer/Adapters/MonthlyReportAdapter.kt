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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.R
import com.teen.videoplayer.Utils.ImageViewerUtils
import com.teen.videoplayer.databinding.DashbardRowLayoutBinding

class MonthlyReportAdapter(
    private val context: Context,
    var items: List<UserDetails>,
    private val onItemClick: (Int,Int) -> Unit

) : RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>() {

    // ViewHolder with ViewBinding
    class ViewHolder(val binding: DashbardRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DashbardRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener {
            onItemClick(position,3)
        }


        holder.binding.apply {
            username.text = item.name
            number.text = item.phone
            if (item.images.isNotEmpty()){
                lastdate.text = item.images[0].date
            }

            edit.setOnClickListener {
                onItemClick(position,1)
            }

            number.setOnClickListener {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${item.phone}")
                }
                context.startActivity(dialIntent)
            }


            delete.setOnClickListener {
                onItemClick(position,2)
            }


            imagefile.setOnClickListener {

                val dataList = item.images.map { image ->
                    data(
                        imageid = image.imageid,
                        date = image.date,
                        file = image.file
                    )
                }


                ImageViewerUtils.fullImageview(context, dataList,startPosition = position)

            }

            if (item.images.isNotEmpty()) {
                Glide.with(context)
                    .load(item.images[0].file)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(imagefile)
            } else {
                imagefile.setImageResource(R.drawable.placeholder_image)
            }


        }
    }

    fun getItem(position: Int): UserDetails = items[position]

    fun updateList(newlist : List<UserDetails>){
        items = newlist
        notifyDataSetChanged()
    }






}
