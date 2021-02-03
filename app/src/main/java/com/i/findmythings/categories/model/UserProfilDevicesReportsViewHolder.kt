package com.i.findmythings.categories.model

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.findmythings.R
import de.hdodenhof.circleimageview.CircleImageView

class UserProfilDevicesReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    lateinit var post_name : TextView
    lateinit var post_lost_place : TextView
    lateinit var post_image_logo : CircleImageView
    lateinit var  editPost : TextView
    lateinit var  deletePost : TextView


    init {
        deletePost = itemView.findViewById(R.id.delete_item_text_profile)
        editPost = itemView.findViewById(R.id.edit_post_text_profile)
    }


    fun setLostName(mname: String){
        post_name = itemView.findViewById(R.id.name_of_post_recycler_text_profile)
        post_name.text = mname
    }

    fun setLostPlace(lostplace: String){
        post_lost_place = itemView.findViewById(R.id.place_of_stolen_hidden_recycler_text_profile)
        post_lost_place.text = lostplace
    }

    fun setLostImageLogo(imageUri: String,context: Context){
        post_image_logo = itemView.findViewById<CircleImageView>(R.id.image_of_post_recycler_profile)
        Glide.with(context).load(imageUri).into(post_image_logo)
    }
}