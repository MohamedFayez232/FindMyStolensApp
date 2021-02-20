package com.i.findmythings.categories.devices.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.categories.devices.model.CategoriesrecyclerModel
import kotlin.collections.ArrayList

class RecyclerLostsCategoriesRepository {


   private var list = ArrayList<CategoriesrecyclerModel> ()


  private fun getcategoryLostItems(serialNumber : String,mainchild:String){
      val ref = Firebase.database.reference


      ref.child("Losts").child(mainchild).addListenerForSingleValueEvent(object : ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                  for (data in snapshot.children){
                      val model = data.getValue(CategoriesrecyclerModel::class.java)
                      if (model!!.serial_number_of_lost.equals(serialNumber)){
                          list.add(model)
                          Log.d("categorylist", "repository size list is:" + list.size)
                      }
                      else{
                          // the serial not foundedd
                          Log.d("categorylist", "user text not matching database serial")
                      }
                  }


              }
              else{
                  // snapshot not founded
                  Log.d("recyclerlostsdata","snapshot not founded")
              }
          }

          override fun onCancelled(error: DatabaseError) {
              TODO("Not yet implemented")
          }

      }


      )
  }

    fun getLostList (serila:String,mainchild:String): MutableLiveData<List<CategoriesrecyclerModel>>{
        getcategoryLostItems(serila,mainchild)
        var livedata : MutableLiveData<List<CategoriesrecyclerModel>> = MutableLiveData()
        livedata.value = list
        return livedata
    }
}