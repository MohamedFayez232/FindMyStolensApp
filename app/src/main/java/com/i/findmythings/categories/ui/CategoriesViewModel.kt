package com.i.findmythings.categories.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.i.findmythings.categories.model.CategoriesrecyclerModel
import com.i.findmythings.categories.repo.RecyclerLostsCategoriesRepository

class CategoriesViewModel : ViewModel() {

     private var repo  : RecyclerLostsCategoriesRepository = RecyclerLostsCategoriesRepository()
     private var lisMutableLiveData : MutableLiveData<List<CategoriesrecyclerModel>> = MutableLiveData()



    fun getAllCategoryDataList(serial:String,mainchild:String):MutableLiveData<List<CategoriesrecyclerModel>>{
         if(lisMutableLiveData == null) {
             lisMutableLiveData = repo.getLostList(serial, mainchild)

         }
        return lisMutableLiveData

    }
}