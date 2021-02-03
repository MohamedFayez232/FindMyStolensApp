package com.i.findmythings.categories.model

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.findmythings.R
import de.hdodenhof.circleimageview.CircleImageView

class LostsCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var name : TextView
    lateinit var lostImage_logo : CircleImageView


    fun setLostName(mname: String){
        name = itemView.findViewById(R.id.lost_item_recycler_name)
        name.text = mname
    }

    fun setLostImageLogo(imageUri: String,context: Context){
        lostImage_logo = itemView.findViewById<CircleImageView>(R.id.losts_logo_image)
        Glide.with(context).load(imageUri).into(lostImage_logo)

    }
}