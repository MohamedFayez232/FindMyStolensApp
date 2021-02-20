package com.i.findmythings.details.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentLostDetailsragmentBinding
import com.i.findmythings.details.models.ImagesFirebaseRecylerDetailsAdapter
import com.i.findmythings.details.models.imagesFirebaseRecyclerModel


class LostDetailsragment : Fragment() {

    lateinit var binding : FragmentLostDetailsragmentBinding
      val args : LostDetailsragmentArgs by navArgs()

    // recyclerview components
    lateinit var adapter : ImagesFirebaseRecylerDetailsAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager
    // args values
    lateinit var main_Child : String
    lateinit var lostSerial_Number : String
    lateinit var publisher_id : String
    lateinit var publisherphone:String

     var imagesList  = ArrayList<imagesFirebaseRecyclerModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lost_detailsragment,
                        container, false)


        binding.namePostingDateProgressBar.visibility = View.VISIBLE
        binding.detailsInfoLayout.visibility = View.INVISIBLE

        main_Child = args.mainchild
        lostSerial_Number = args.lostSerialNumberAsId
        publisher_id = args.publisherId

        getLostImages(lostSerial_Number)
        getPublisherData( publisher_id)

        if (main_Child.equals("LabTobs")){
            binding.middleDeatislLayout.serialIc.setImageResource(R.drawable.labtop_type_ic)
            getDevicesData(main_Child,lostSerial_Number)

        }
        else if( main_Child.equals("Phones")){
            binding.middleDeatislLayout.serialIc.setImageResource(R.drawable.phone_type_ic)
            getDevicesData(main_Child,lostSerial_Number)

        }
        else if( main_Child.equals("Cars_Vichels")){
            binding.middleDeatislLayout.serialIc.setImageResource(R.drawable.car_type_ic)

            getDevicesData(main_Child,lostSerial_Number)
        }

        else if( main_Child.equals("Childrens")){


            // srial number will be in children case to id national
            getChildrenDeatials(lostSerial_Number)
        }


        implemntPostOptions()
        setUpNavigations()
        return binding.root
    }

    private fun setUpNavigations(){
        binding.backImageToolbarDetails.visibility = View.INVISIBLE
        binding.fragmentNameToolbarDetails.text =resources.getString(R.string.details_fragment)
    }


    private fun implemntPostOptions(){
        binding.topDeatilsLayout.contactPostPublisherDetailsIc.setOnClickListener {

            val intent :Intent= Intent().apply {
                 action = Intent.ACTION_DIAL
                data = (Uri.parse("tel:$publisherphone"))

            }

                startActivity(intent)

        }


        binding.topDeatilsLayout.sharePostDetailsIc.setOnClickListener {

            val intent :Intent= Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.shareing_post))
            }
            var shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)

        }

        binding.topDeatilsLayout.reportPostDetailsIc.setOnClickListener {
          Toast.makeText(requireActivity(),resources.getString(R.string.cant_report_now),
          Toast.LENGTH_SHORT).show()

            val action = LostDetailsragmentDirections
                .actionLostDetailsragmentToPostReporFragment(lostSerial_Number)
              findNavController().navigate(action)
        }
    }

   private fun getDevicesData(mainchild:String , serailNumber:String){

       val ref = Firebase.database.reference.child("All_Losts").child(mainchild)
               .child(serailNumber)
       ref.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val lostName = snapshot.child("name_of_lost").getValue(String::class.java)
               binding.lostNameTextDetails.text = lostName

               val stolendate = snapshot.child("date_of_stolen").getValue(String::class.java)
               binding.middleDeatislLayout.actualSdateTextDetails.text = stolendate

               binding.middleDeatislLayout.actualSerialTextDetails.text = serailNumber

               val stolen_place = snapshot.child("place_of_stolen").getValue(String::class.java)
               binding.middleDeatislLayout.actualSplaceTextDetails.setText(stolen_place)

               val posting_date = snapshot.child("posting_date").getValue(String::class.java)
               binding.actualPostingDateTextDetailsBelowTittle.text = posting_date

               val stolen_color = snapshot.child("lost_color").getValue(String::class.java)
               binding.middleDeatislLayout.actualColorDetails.text = stolen_color


               val stolen_type = snapshot.child("type").getValue(String::class.java)
               binding.middleDeatislLayout.actualTypeTextDetails.text = stolen_type

               binding.namePostingDateProgressBar.visibility = View.GONE
               binding.detailsInfoLayout.visibility = View.VISIBLE

           }

           override fun onCancelled(error: DatabaseError) {

           }

       })
    }


    private fun getChildrenDeatials(serailNumber:String){
        binding.middleDeatislLayout.deviceDeatilsHeader.setText(resources.getString(R.string.children_info))
        binding.middleDeatislLayout.dateOfStolenTittleDetails.setText(resources.getString(R.string.children_hidden_date))
        binding.middleDeatislLayout.placeOfStolenTittleDetails.setText(resources.getString(R.string.children_hidden_place))
        binding.middleDeatislLayout.serialNumerTittleDetails.setText(resources.getString(R.string.nattional_idd_tittle))
        binding.middleDeatislLayout.colorTittleTextDetails.setText(resources.getString(R.string.children_pirthdate))
        binding.middleDeatislLayout.dateOfStolenTittleDetails.setText(resources.getString(R.string.children_hidden_date))
        binding.middleDeatislLayout.colorIc.setImageResource(R.drawable.birthday_icon)
        binding.middleDeatislLayout.typeIc.setImageResource(R.drawable.gender_ic)

        val ref = Firebase.database.reference.child("All_Losts").child("Childrens")
            .child(serailNumber)
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {


                val childrenName = snapshot.child("name_of_children").getValue(String::class.java)
                binding.lostNameTextDetails.text = childrenName

               // val national_id = snapshot.child("children_nationa_id").getValue(String::class.java)
                binding.middleDeatislLayout.actualSerialTextDetails.setText(getString(R.string.nationaId_children_privete))

                val stolendate = snapshot.child("date_of_hidden").getValue(String::class.java)
                binding.middleDeatislLayout.actualSdateTextDetails.text = stolendate

                binding.middleDeatislLayout.actualSerialTextDetails.text = resources.getString(R.string.nationaId_children_privete)

                val stolen_place = snapshot.child("place_of_hidden").getValue(String::class.java)
                binding.middleDeatislLayout.actualSplaceTextDetails.setText(stolen_place)

                val posting_date = snapshot.child("posting_date").getValue(String::class.java)
                binding.actualPostingDateTextDetailsBelowTittle.text = posting_date

                val children_pirthday = snapshot.child("children_pirthday").getValue(String::class.java)
                binding.middleDeatislLayout.actualColorDetails.text = children_pirthday


                val children_type = snapshot.child("type").getValue(String::class.java)
                binding.middleDeatislLayout.actualTypeTextDetails.text = children_type

                binding.namePostingDateProgressBar.visibility = View.GONE
                binding.detailsInfoLayout.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    // this is to display losted item images as a slider
    private fun getLostImages(serialNumber:String){
        layoutManager = GridLayoutManager(requireActivity() , 1,GridLayoutManager.HORIZONTAL ,false)
        binding.imagesDetailsSliderRecycler.layoutManager = layoutManager
        adapter = ImagesFirebaseRecylerDetailsAdapter(imagesList, requireActivity())
        binding.imagesDetailsSliderRecycler.adapter = adapter


        val ref = Firebase.database.reference.child("lost_images")
                .child(serialNumber)

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in  snapshot.children){
                        val dataclassmodel = data.getValue(imagesFirebaseRecyclerModel::class.java)
                        if (dataclassmodel != null) {
                            imagesList.add(dataclassmodel)
                            adapter.notifyDataSetChanged()

                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun getPublisherData( publisher_id:String){
        val ref = Firebase.database.reference.child("Users").child(publisher_id)
            .child("basic_info")
        
           ref.addListenerForSingleValueEvent(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   val publisherName = snapshot.child("user_name").getValue(String::class.java)
                   binding.middleDeatislLayout.actualPunlisherNameTextDetails.text = publisherName

                   val publisheraddress = snapshot.child("user_address").getValue(String::class.java)
                   binding.middleDeatislLayout.actualPunlisherAddressTextDetails.text = publisheraddress

                    publisherphone = snapshot.child("user_phone").getValue(String::class.java)!!
                   binding.middleDeatislLayout.actualPunlisherPhoneTextDetails.text = publisherphone
               }

               override fun onCancelled(error: DatabaseError) {

               }

           })
    }




}