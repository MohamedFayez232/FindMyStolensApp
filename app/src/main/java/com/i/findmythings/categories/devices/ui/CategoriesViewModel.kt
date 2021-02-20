package com.i.findmythings.categories.devices.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.i.findmythings.categories.devices.model.CategoriesrecyclerModel
import com.i.findmythings.categories.devices.repo.RecyclerLostsCategoriesRepository

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