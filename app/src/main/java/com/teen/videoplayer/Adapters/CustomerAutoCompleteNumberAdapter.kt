package com.teen.videoplayer.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.teen.videoplayer.Model.UserDetails
import com.teen.videoplayer.R
import de.hdodenhof.circleimageview.CircleImageView


class CustomerAutoCompleteNumberAdapter(
    context: Context,
    private var customers: MutableList<UserDetails>
) : ArrayAdapter<UserDetails>(context, 0, customers) {

    fun updateData(newData: MutableList<UserDetails>) {
        customers = newData
        clear()
        addAll(newData)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = customers.size

    override fun getItem(position: Int): UserDetails? = customers[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_customer_suggestion, parent, false
        )

        val name = view.findViewById<TextView>(R.id.tvName)
        val number = view.findViewById<TextView>(R.id.tvNumber)
        val imageview = view.findViewById<CircleImageView>(R.id.imageview)



        if (item != null) {
            Glide
                .with(context)
                .load(item.file)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(imageview)
        }

        name.text = item?.name
        number.text = item?.phone

        return view
    }
}
