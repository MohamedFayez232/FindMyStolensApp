package com.i.findmythings.categories.model

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.findmythings.R
import de.hdodenhof.circleimageview.CircleImageView

class ChildrensLostRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var children_name : TextView
    lateinit var children_lost_place : TextView
    lateinit var children_image_logo : CircleImageView

    fun setLostName(mname: String){
        children_name = itemView.findViewById(R.id.name_of_children_recycler_text)
        children_name.text = mname
    }

    fun setLostPlace(lostplace: String){
        children_lost_place = itemView.findViewById(R.id.place_of_children_hidden_recycler_text)
        children_lost_place.text = lostplace
    }

    fun setLostImageLogo(imageUri: String,context: Context){
        children_image_logo = itemView.findViewById<CircleImageView>(R.id.image_of_children_recycler)
        Glide.with(context).load(imageUri).into(children_image_logo)
    }
}