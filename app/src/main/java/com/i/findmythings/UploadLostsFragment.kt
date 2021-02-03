package com.i.findmythings

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.i.findmythings.add.ui.AddLostFragmentDirections
import com.i.findmythings.add.ui.AddLostsViewModel
import com.i.findmythings.add.ui.ImagesRecylerAdapter
import com.i.findmythings.databinding.FragmentUploadLostsBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class UploadLostsFragment : Fragment() {

    private val args : UploadLostsFragmentArgs by navArgs()

    lateinit var devicename : String
    lateinit var deviceSerial : String
    lateinit var type : String
    lateinit var StolenPlace : String
    lateinit var StolenDate : String
    lateinit var deviceColor : String
    lateinit var MainChild : String

    val GALLERY_PERMISSION_REQUEST = 1


    lateinit var binding : FragmentUploadLostsBinding

      lateinit var viewModel : AddLostsViewModel

    // this save images uris when user select  multiable images from garally

    var imageslistUri = ArrayList<Uri>()
    var imagesListRecycler = ArrayList<imagesRecyclerModel>()

    // this first image uri when user select  multiable images from garally

    lateinit var FirstImageUriInList : String

    // this indvidual image uri when user select  mu;tiable image from garally

    lateinit var imageUri :Uri
    lateinit var loadingBar : ProgressDialog

    // this when user select only one image from garally
   lateinit var mimageUri : Uri

     val OPEN_GALLERY_IMAGE_REQUEST_CODE : Int = 0
     var checkpermissionsGranted = false

    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : ImagesRecylerAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // inflate layout
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_upload_losts,
                container, false)


        binding.sendDataButton.isClickable = false
        binding.sendDataButton.isEnabled = false


        // inti viewModel
        viewModel = ViewModelProviders.of(this).get(AddLostsViewModel::class.java)
        loadingBar = ProgressDialog(requireActivity())
        binding.addImagesIc.setOnClickListener { View ->
            OpenGallery()
        }


        //binding.imageSwitcher.setFactory{ ImageView(requireContext().applicationContext) }
        layoutManager = LinearLayoutManager(requireActivity())
        adapter =  ImagesRecylerAdapter(imagesListRecycler, requireActivity())
        binding.imagesListRecyclerview.layoutManager = layoutManager
        binding.imagesListRecyclerview.adapter = adapter


        // args
        devicename = args.deviceName
        deviceSerial =args.deviceSerialNumber
        deviceColor =args.deviceColor
        type =args.VinNumber
        StolenDate =args.stolenDate
        StolenPlace =args.SoltenPlace
        MainChild =args.mainChild

        Log.d("safeargsrelated", "serial upload fragment :" + deviceSerial)


        setUpNavigations()
        return binding.root
    }

    private fun OpenGallery() {
     if(checkpermissionsGranted){
         startGalleryIntent()
     }
        else{
         CheckGalleryPermission()
        }
    }

    /**@
     *
     */
    private fun startGalleryIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                OPEN_GALLERY_IMAGE_REQUEST_CODE)
    }

    /**@author
     *
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // first if user select more than 1 image
        if (requestCode == OPEN_GALLERY_IMAGE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if(data!!.clipData != null){
                    val count = data.clipData!!.itemCount

                   for(i in 0 until count){
                        imageUri = data.clipData!!.getItemAt(i).uri
                       imageslistUri.add(imageUri)

                       // this to add images to recyclerview
                       var uriString = imageslistUri.get(i).toString()
                       val images = imagesRecyclerModel(uriString,uriString)
                       imagesListRecycler.add(images)
                       adapter.notifyDataSetChanged()

                       binding.sendDataButton.isClickable = true
                       binding.sendDataButton.isEnabled = true
                   }

                    /*// display first image from this list in image swithcer
                    binding.imageSwitcher.setImageURI(imageslistUri[0])*/

                    binding.sendDataButton.setOnClickListener{
                        getDownloadUriMultiableImagesSelected()
                    }
                }

                // this if user choose only one image
                else{
                    mimageUri = data.data!!
                    Log.d("imageuri", "chossen image uri not method  :" + mimageUri)

                   // binding.imageSwitcher.setImageURI(mimageUri)
                    val uriString = mimageUri.toString()
                    val images = imagesRecyclerModel(uriString,uriString)
                    imagesListRecycler.add(images)
                    adapter.notifyDataSetChanged()
                    binding.sendDataButton.isClickable = true
                    binding.sendDataButton.isEnabled = true

                    binding.sendDataButton.setOnClickListener{
                        saveSinglImageSelected()
                    }

                }
            }
        }

    }


    /*
    for singl image get down;oad uri
     */

    private fun saveSinglImageSelected(){

       val storageRef =  FirebaseStorage.getInstance().getReference()
        val ref = storageRef.child("User_Lost_Images")
                .child("Image" + mimageUri.lastPathSegment + ".jpg")

        ref.putFile(mimageUri).addOnSuccessListener {
            ref.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val ImageUri = task.result.toString()
                    StoreSinglImagesUri(ImageUri)
                }
            }
        }

    }

/*
for mutables image get down;oad uri
 */
    private fun getDownloadUriMultiableImagesSelected(){
           // Log.d("imageuri", "first Image in list :" + FirstImageUriInList)
          StoreImagesUris(imageslistUri)
    }


    /*
    for mutables images storing
     */
    private fun StoreImagesUris(imagesUriList : ArrayList<Uri>){
        viewModel.UploadMutlableImagesUri(imagesUriList, devicename,
                deviceSerial, deviceColor, StolenPlace, StolenDate, type, MainChild,loadingBar)
    }


    /*
    for singl image storing
     */
    private fun StoreSinglImagesUri(Imageuri: String){
        viewModel.uploadSinglImageSelected(Imageuri, devicename,
                deviceSerial, deviceColor, StolenPlace, StolenDate, type, MainChild,loadingBar)
    }


    private fun setUpNavigations(){
        binding.backImageToolbar.setOnClickListener {
            val action = UploadLostsFragmentDirections.actionUploadLostsFragmentToAddLostFragment()
            findNavController().navigate(action)
        }

        binding.fragmentNameToolbar.visibility = View.INVISIBLE
    }


    /**@author
     *
     */
    private fun CheckGalleryPermission() {
        Log.d("locationpermissions", "check permissions")
        checkpermissionsGranted = false
        Dexter.withContext(requireActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Log.d("locationpermissions", "permissions granted")
                        checkpermissionsGranted = true
                        Log.d("locationpermissions", "permissions granted" + checkpermissionsGranted)


                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        Log.d("locationpermissions", "permissions denied")
                        checkpermissionsGranted = false
                        Log.d("locationpermissions", "permissions denied" + checkpermissionsGranted)
                        showSettingsDialog()

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) { /* ... */
                        token?.continuePermissionRequest()
                    }
                }).check()

    }



    /*
      this show dialog tells user that accepting permmissions is mustly,
      it implemented when user reject any permission

       */
    private fun showSettingsDialog() {
        Log.e("locationpermissions", "alert dialog method calling ,permissions denied")
        var builder : AlertDialog.Builder  =  AlertDialog.Builder(requireActivity());
        builder.setTitle(resources.getString(R.string.permissions_required));
        builder.setMessage(resources.getString(R.string.permissions_info));
        builder.setPositiveButton(
                resources.getString(R.string.ok_button),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                    CheckGalleryPermission()
                })

        builder.show();
    }


    override fun onStart() {
        super.onStart()
        CheckGalleryPermission()
    }
}