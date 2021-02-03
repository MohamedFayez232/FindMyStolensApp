package com.i.findmythings.home.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.i.findmythings.R

class MainCategoryRecyclerAdapter() : RecyclerView.Adapter<MainCategoryRecyclerAdapter.ViewHolder>() {
    var list = ArrayList<MainCategoryModel>()
    lateinit var context: Context

    private var itemListener: RecyclerViewClickListener? = null


    constructor(list: ArrayList<MainCategoryModel>, listner: RecyclerViewClickListener,
        context: Context
    ) : this() {
        this.list = list
        this.context = context
        this.itemListener = listner
    }

    interface RecyclerViewClickListener {
        fun recyclerViewListClicked(v: View?, position: Int)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCategoryRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.home_category_recycler_layout,
            parent,
            false)
        return ViewHolder(v, itemListener)
    }

    override fun onBindViewHolder(holder: MainCategoryRecyclerAdapter.ViewHolder, position: Int) {
        var categoryobject = list[position]

        holder.name.text = categoryobject.categoryName
         holder.image.setImageResource(categoryobject.CategoryImage)

    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun recyclerFilter(categoryNameList : ArrayList<MainCategoryModel> ){
        this.list = categoryNameList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, itemListenerinterface: RecyclerViewClickListener?) : RecyclerView.ViewHolder(itemView){

        lateinit var name : TextView
        lateinit var image : ImageView

        init {
            name = itemView.findViewById(R.id.category_name)
            image = itemView.findViewById(R.id.category_image)
            itemView.setOnClickListener {
                itemListenerinterface?.recyclerViewListClicked(itemView,adapterPosition)
            }
        }



    }
}