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
import com.teen.videoplayer.Model.Usermonthly
import com.teen.videoplayer.Model.data
import com.teen.videoplayer.R
import com.teen.videoplayer.databinding.DashbardRowLayoutBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MonthlyReportAdapter(
    private val context: Context,
    private var items: List<Usermonthly>,
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
            lastdate.text = item.created_at
            number.text = item.phone



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
                val imageList = items.map { it.file ?: "" }


                val dataList = items.mapNotNull {
                    val date = item.created_at
                    val file = it.file
                    if (!file.isNullOrEmpty()) data(date, file) else null
                }


                fullImageview(
                    imageList = dataList,
                    startPosition = position
                )
            }

            Glide
                .with(context)
                .load(item.file)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(imagefile)


        }
    }

    fun updateList(newlist : List<Usermonthly>){
        items = newlist
        notifyDataSetChanged()
    }



    fun fullImageview(imageList: List<data>,  startPosition: Int = 0) {

        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_full_image, null)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.imageViewPager)
        val crossbutton = dialogView.findViewById<CardView>(R.id.crossbutton)
        val maxlayout = dialogView.findViewById<CardView>(R.id.maxlayout)
        val imgToggleSize = dialogView.findViewById<ImageView>(R.id.imgToggleSize)




        var isFullScreen = false



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


}
