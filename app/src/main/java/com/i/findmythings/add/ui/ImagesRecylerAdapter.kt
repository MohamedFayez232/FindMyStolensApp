package com.i.findmythings.add.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.findmythings.R
import com.i.findmythings.imagesRecyclerModel

class ImagesRecylerAdapter() : RecyclerView.Adapter<ImagesRecylerAdapter.ViewHolder>() {
    var ImagesList = ArrayList<imagesRecyclerModel>()
    lateinit var context: Context

    constructor(ImagesList :ArrayList<imagesRecyclerModel>, context: Context) : this() {
        this.ImagesList = ImagesList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      var v = LayoutInflater.from(context).inflate(R.layout.images_recycler_layour, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val currentItem = ImagesList[position]
        holder.imageName_text.text = currentItem.imageName

      Glide.with(context).load(currentItem.imageUri).into(holder.selectedimageview)
        holder.delete_image_icon.setOnClickListener {
            val mposition = ImagesList.get(position)
            ImagesList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeRemoved(position,ImagesList.size)
            Toast.makeText(context,"Removed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return ImagesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var selectedimageview: ImageView
        lateinit var delete_image_icon: ImageView
        lateinit var imageName_text: TextView

        init {
            selectedimageview = itemView.findViewById(R.id.image_imageview_imagesRecycler)
            delete_image_icon = itemView.findViewById(R.id.delete_image_ic)
            imageName_text = itemView.findViewById(R.id.name_of_image)
        }

    }
}