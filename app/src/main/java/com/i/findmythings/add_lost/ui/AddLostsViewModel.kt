package com.i.findmythings.add_lost.ui

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.i.findmythings.add_lost.repo.UploadLostInfoRepo

class AddLostsViewModel : ViewModel() {

     var repo : UploadLostInfoRepo = UploadLostInfoRepo()
     var singleImageLiveData : MutableLiveData<String> = MutableLiveData()
     var mutiableImageLiveData : MutableLiveData<String> = MutableLiveData()




    // this function to set an andvidual image uri from list , and also get first image uri in list
    fun UploadMutlableImagesUri (imagesUriList : ArrayList<Uri>, dName : String,
                                 dSerial : String, dColor : String, Splace : String, Sdate : String,
                                 type : String, MainChild : String, loadingBar:ProgressDialog,
                                 mcontext :Context) : MutableLiveData<String> {

        repo.uploadMutiableDataImages(imagesUriList,dName,dSerial,dColor,
                Splace,Sdate,type,MainChild,loadingBar, mcontext)


        mutiableImageLiveData.value = dName
        mutiableImageLiveData.value = dSerial
        mutiableImageLiveData.value = dColor
        mutiableImageLiveData.value = Splace
        mutiableImageLiveData.value = Sdate
        mutiableImageLiveData.value = type
        mutiableImageLiveData.value = MainChild
        Log.d("safeargsrelated", "serial viewmodel :" + dSerial)
        return mutiableImageLiveData



    }



    // this is if user chose only one image and upload it
    fun uploadSinglImageSelected (imageUri:String, dName: String,
                                  dSerial : String, dColor : String, Splace : String, Sdate : String,
                                  type : String, MainChild : String, loadingBar:ProgressDialog,
                                  mcontext :Context) {

        repo.uploadSingleDataImages(imageUri,dName,dSerial,dColor,
                Splace,Sdate,type,MainChild,loadingBar,mcontext)

    }

}