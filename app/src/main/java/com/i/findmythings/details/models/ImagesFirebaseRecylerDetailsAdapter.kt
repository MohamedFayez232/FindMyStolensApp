package com.i.findmythings.details.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.i.findmythings.R

class ImagesFirebaseRecylerDetailsAdapter() : RecyclerView.Adapter<ImagesFirebaseRecylerDetailsAdapter.ViewHolder>() {
    var ImagesList = ArrayList<imagesFirebaseRecyclerModel>()
    lateinit var context: Context

    constructor(ImagesList :ArrayList<imagesFirebaseRecyclerModel>, context: Context) : this() {
        this.ImagesList = ImagesList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      var v = LayoutInflater.from(context).inflate(R.layout.images_firebase_recycler_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val currentItem = ImagesList[position]


      Glide.with(context).load(currentItem.image_uri) // resize image
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .into(holder.selectedimageview)

    }

    override fun getItemCount(): Int {
        return ImagesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var selectedimageview: ImageView


        init {
            selectedimageview = itemView.findViewById(R.id.image_imageview_firebase_Recycler)

        }

    }
}