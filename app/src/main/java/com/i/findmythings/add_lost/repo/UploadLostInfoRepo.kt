package com.i.findmythings.add_lost.repo

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.i.findmythings.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UploadLostInfoRepo {

  private var mutableImageLiveData : MutableLiveData<String> = MutableLiveData()
    private var singleImageLiveData : MutableLiveData<String> = MutableLiveData()



   private fun UploadMutiableImages(imagesUriList : ArrayList<Uri>, dName: String,
          dSerial: String, dColor: String, Splace: String, Sdate: String,
          type: String, MainChild: String, loadingBar:ProgressDialog,mcontext :Context){
       loadingBar.setTitle("جاري الارسال")
       loadingBar.setMessage("برجاء الانتظار قليلا")
       loadingBar.setCanceledOnTouchOutside(false)
       loadingBar.show()
       Log.d("safeargsrelated", "serial repo hashMap method:" + dSerial)
       var storegeRef = FirebaseStorage.getInstance().getReference().child("User_Lost_Images")
       for(i in 0 until imagesUriList.size){
           val indivdualImageUri = imagesUriList.get(i)
           val imagesNames = storegeRef.child("Image" + indivdualImageUri.lastPathSegment)
           val uploadTask = imagesNames.putFile(indivdualImageUri)
           uploadTask.continueWith { task ->
               if (!task.isSuccessful){
                   task.exception?.let {
                       throw it
                   }
               }
               imagesNames.downloadUrl.addOnCompleteListener { task ->
                   if (task.isSuccessful){
                       val IndividualImageUri = task.result.toString()
                       val firstImageUri = IndividualImageUri.toString()

                       // current date to record time and date of recording lost
                       var s : SimpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
                       val dateTime = s.format(Date())


                       var imagesHashMap : HashMap<String, Any> = HashMap<String, Any> ()
                       imagesHashMap.put("image_uri", IndividualImageUri)


                       var hashMap : HashMap<String, Any> = HashMap<String, Any> ()

                       if(MainChild.equals("Childrens")) {
                           hashMap.put("name_of_children", dName)
                           hashMap.put("children_nationa_id", dSerial)
                           hashMap.put("place_of_hidden", Splace)
                           hashMap.put("date_of_hidden", Sdate)
                           hashMap.put("posting_date", dateTime)
                           hashMap.put("children_pirthday", dColor)
                           hashMap.put("type", type)
                           hashMap.put("category", MainChild)
                           hashMap.put("publisher_id", Firebase.auth.currentUser!!.uid.toString())
                           hashMap.put("lost_logo_image_uri", firstImageUri)

                           uploadAllDeviceData(hashMap, imagesHashMap, dSerial, MainChild, loadingBar,mcontext)
                       }
                       else{
                           hashMap.put("name_of_lost", dName)
                           hashMap.put("date_of_stolen", Sdate)
                           hashMap.put("serial_number_of_lost", dSerial)
                           hashMap.put("place_of_stolen", Splace)
                           hashMap.put("posting_date", dateTime)
                           hashMap.put("lost_color", dColor)
                           hashMap.put("type", type)
                           hashMap.put("category", MainChild)
                           hashMap.put("publisher_id", Firebase.auth.currentUser!!.uid.toString())
                           hashMap.put("lost_logo_image_uri", firstImageUri)


                           uploadAllDeviceData(hashMap, imagesHashMap, dSerial, MainChild,loadingBar, mcontext)
                       }
                   }

               }
           }

       }


    }



    private  fun UploadSignaldatImageSelected(imageUri: String, dName: String,
         dSerial: String, dColor: String, Splace: String, Sdate: String,
         type: String, MainChild: String, loadingBar:ProgressDialog,mcontext : Context){

        loadingBar.setTitle("جاري الارسال")
        loadingBar.setMessage("برجاء الانتظار قليلا")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        // current date to record time and date of recording lost
        var s : SimpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        val dateTime = s.format(Date())


        var imagesHashMap : HashMap<String, Any> = HashMap<String, Any> ()
        imagesHashMap.put("image_uri", imageUri)


        var hashMap : HashMap<String, Any> = HashMap<String, Any> ()
         if(MainChild.equals("Childrens")) {
             hashMap.put("name_of_children", dName)
             hashMap.put("children_nationa_id", dSerial)
             hashMap.put("place_of_hidden", Splace)
             hashMap.put("date_of_hidden", Sdate)
             hashMap.put("posting_date", dateTime)
             hashMap.put("children_pirthday", dColor)
             hashMap.put("type", type)
             hashMap.put("category", MainChild)
            hashMap.put("publisher_id", Firebase.auth.currentUser!!.uid.toString())
            hashMap.put("lost_logo_image_uri", imageUri)

            uploadAllDeviceData(hashMap, imagesHashMap, dSerial, MainChild, loadingBar, mcontext)

        }
        else{
            hashMap.put("name_of_lost", dName)
            hashMap.put("date_of_stolen", Sdate)
            hashMap.put("serial_number_of_lost", dSerial)
            hashMap.put("place_of_stolen", Splace)
            hashMap.put("posting_date", dateTime)
            hashMap.put("lost_color", dColor)
            hashMap.put("type", type)
             hashMap.put("category", MainChild)
            hashMap.put("publisher_id", Firebase.auth.currentUser!!.uid.toString())
            hashMap.put("lost_logo_image_uri", imageUri)

             uploadAllDeviceData(hashMap, imagesHashMap, dSerial, MainChild, loadingBar,mcontext)
        }
    }


    private fun uploadAllDeviceData(datahasmap: HashMap<String, Any>,
        imageshasmap: HashMap<String, Any>, dSerial: String, MainChild: String,loadingBar:ProgressDialog,
                             mcontext :Context){
        // database refrences
        var ref = Firebase.database.reference
        val imagesRef = Firebase.database.reference.child("lost_images").child(dSerial).push()


        ref.child("All_Losts").child(MainChild).child(dSerial)
           .updateChildren(datahasmap).addOnCompleteListener { task ->
             if (task.isSuccessful) {
              Log.d("recirdlosts", "recoring lost to losts category, done")

                imagesRef.setValue(imageshasmap).addOnCompleteListener { task ->
                 if (task.isSuccessful){
                   if (MainChild.equals("Childrens")){
                    // insert data to user account
                      ref .child("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                         .child("user_recorded_losts").child(MainChild).child(dSerial)
                          .updateChildren(datahasmap)
                         .addOnCompleteListener { task ->
                          if (task.isSuccessful) {

                          }
                         }
                         Log.d("insertlostdata","  inserting all data done")
                         loadingBar.dismiss()
                          }
                          else{
                          // insert data to user account
                           ref .child("Users")
                                   .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                   .child("user_recorded_losts").child("devices_reports")
                                   .child(dSerial).updateChildren(datahasmap).addOnCompleteListener { task ->
                                       if (task.isSuccessful){
                                           loadingBar.dismiss()
                                           Toast.makeText(mcontext,
                                            mcontext.resources.getString(R.string.uploading_sucess),Toast.LENGTH_SHORT)
                                                   .show()

                                       }
                                       else{
                                           Toast.makeText(mcontext,
                                                   mcontext.resources.getString(R.string.uploading_Not_sucess),
                                                   Toast.LENGTH_SHORT)
                                           .show()
                                       }
                                   }
                          }
                 }




                }


             }
                else{
                 Log.d("insertlostdata","error in inserting all data ")
                 loadingBar.dismiss()
                    }

                }



    }



    /*
     this is to upload mutable images
     */
    fun uploadMutiableDataImages(imagesUriList : ArrayList<Uri>, dName: String,
                                 dSerial: String, dColor: String, Splace: String, Sdate: String,
                                 type: String, MainChild: String, loadingBar:ProgressDialog,mcontext :Context) : MutableLiveData<String>{

        UploadMutiableImages(imagesUriList, dName, dSerial, dColor, Splace,
                Sdate, type, MainChild,loadingBar,mcontext)
        mutableImageLiveData.value = dName
        mutableImageLiveData.value = dSerial
        mutableImageLiveData.value = dColor
        mutableImageLiveData.value = Splace
        mutableImageLiveData.value = Sdate
        mutableImageLiveData.value = type
        mutableImageLiveData.value = MainChild
        Log.d("safeargsrelated", "serial repo live data method :" + dSerial)
        return mutableImageLiveData

    }


    /*
 this is to upload mutable images
 */
    fun uploadSingleDataImages(imageUri: String, dName: String,
           dSerial: String, dColor: String, Splace: String, Sdate: String,
           type: String, MainChild: String, loadingBar:ProgressDialog,mcontext :Context) : MutableLiveData<String>{

        UploadSignaldatImageSelected(imageUri, dName, dSerial, dColor, Splace, Sdate, type, MainChild
        ,loadingBar,mcontext)

        singleImageLiveData.value = imageUri
        singleImageLiveData.value = dName
        singleImageLiveData.value = dSerial
        singleImageLiveData.value = dColor
        singleImageLiveData.value = Splace
        singleImageLiveData.value = Sdate
        singleImageLiveData.value = type
        singleImageLiveData.value = MainChild

        return singleImageLiveData

    }


}