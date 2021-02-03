package com.i.findmythings.user.userediting.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.UploadLostsFragmentDirections
import com.i.findmythings.add.ui.AddLostFragmentDirections
import com.i.findmythings.databinding.FragmentEditUserPostsBinding
import kotlinx.android.synthetic.main.images_firebase_recycler_layout.*


class EditUserPostsFragment : Fragment() {

        lateinit var binding : FragmentEditUserPostsBinding
        val  args : EditUserPostsFragmentArgs by navArgs()


    lateinit var nameOfLost : String
    lateinit var dateOfLost : String
    lateinit var placeOfLost : String
    lateinit var colorOfLost : String
    lateinit var type : String

      lateinit var postId : String
      lateinit var MainChild : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil. inflate(inflater,R.layout.fragment_edit_user_posts, container, false)

        postId = args.postId
        MainChild = args.postCategory

        binding.updatePostProgressbar.visibility = View.INVISIBLE

        // cancel this fragment and return to previous


        // update button
        binding.updatePostButton.setOnClickListener {
            invalidatatons()
        }

        setUpNavigations()


        if (MainChild.equals("Childrens")){
            getChildrenInf(MainChild)
        }

        else if(MainChild.equals("Phones")) {
            getDeviceInf(MainChild)
        }
        else if(MainChild.equals("LabTobs")){
            getDeviceInf(MainChild)
        }
        else if(MainChild.equals("Cars_Vichels")){
            getDeviceInf(MainChild)
        }

        return binding.root
    }

    private fun setUpNavigations(){
        binding.backImageToolbar.setOnClickListener {
            val action = EditUserPostsFragmentDirections.actionEditUserPostsFragmentToUserProfileFragment()
            findNavController().navigate(action)
        }

        binding.fragmentNameToolbar.text = resources.getString(R.string.edituser_post_fragment)
    }
    /**@author
     *
     */
    private fun invalidatatons() {

        if (MainChild.equals("Cars_Vichels") or MainChild.equals("Childrens") or  MainChild.equals("LabTobs") or
                MainChild.equals("Phones")) {

            nameOfLost = binding.updateNameOfLost.text.toString()
            dateOfLost = binding.updateDateOfStolen.text.toString()
            placeOfLost = binding.updatePlaceOfStolen.text.toString()
            colorOfLost = binding.updateColorOfStolen.text.toString()
            type = binding.updateStolenType.text.toString()


            if (TextUtils.isEmpty(nameOfLost)) {
                binding.updateNameOfLost.setError("")
            }  else if (TextUtils.isEmpty(dateOfLost)) {
                binding.updateDateOfStolen.setError("")
            } else if (TextUtils.isEmpty(placeOfLost)) {
                binding.updatePlaceOfStolen.setError("")
            } else if (TextUtils.isEmpty(colorOfLost)) {
                binding.updateColorOfStolen.setError("")

            }
            else if (TextUtils.isEmpty(type)) {
                binding.updateStolenType.setError("")

            }
            else {
                //  UploadInfoToFirebase(nameOfLost, dateOfLost,serialOfLost, placeOfLost,colorOfLost,vinNinmberOfLost)
                UpdateToFirebase(nameOfLost, colorOfLost, type,
                        placeOfLost, dateOfLost, MainChild)
            }
        }
        else{
            // user dosent choose a category
            Toast.makeText(requireActivity(),R.string.validate_spinner, Toast.LENGTH_SHORT).show()

        }
    }

    /**@author
     *
     */
    private fun UpdateToFirebase(updatedname:String, updatedcolor:String, updatedtype:String,
                                 updatedplace:String, updateddate:String, mainChild:String){

        if(MainChild.equals("Cars_Vichels")or MainChild.equals("LabTobs") or
                MainChild.equals("Phones")){
            UploadupdatedDevicesInfo(updatedname,updatedcolor,updatedtype,updatedplace,updateddate,mainChild)
        }

        else{
            UploadupdatedChildrensInfo(updatedname,updatedcolor,updatedtype,updatedplace,updateddate,mainChild)
        }

    }


    /**@author
     *
     */
    private fun UploadupdatedDevicesInfo(updatedname:String, updatedcolor:String, updatedtype:String,
                                         updatedplace:String, updateddate:String, mainChild:String){

        binding.updatePostProgressbar.visibility = View.VISIBLE

        var deviceshashMap : HashMap<String, Any> = HashMap<String, Any> ()
        deviceshashMap.put("name_of_lost", updatedname)
        deviceshashMap.put("date_of_stolen", updateddate)
        deviceshashMap.put("place_of_stolen", updatedplace)
        deviceshashMap.put("lost_color", updatedcolor)
        deviceshashMap.put("type", updatedtype)

        val ref = Firebase.database.reference.child("All_Losts").child(mainChild)
                .child(postId).updateChildren(deviceshashMap).addOnCompleteListener{task ->
                   if (task.isSuccessful){
                       val ref = Firebase.database.reference.child("Users")
                         .child(FirebaseAuth.getInstance().currentUser!!.uid)
                               .child("user_recorded_losts").child("devices_reports")
                               .child(postId).updateChildren(deviceshashMap).addOnCompleteListener{task ->
                                   if (task.isSuccessful){
                                       Toast.makeText(requireActivity(),R.string.post_updated, Toast.LENGTH_SHORT).show()
                                       binding.updatePostProgressbar.visibility = View.GONE
                                   }
                                   else{
                                       Log.d("updatingdevicesinfo","erroe while update device info in user_losts")

                                       Toast.makeText(requireActivity(),R.string.post_updated_error, Toast.LENGTH_SHORT).show()
                                       binding.updatePostProgressbar.visibility = View.GONE

                                   }
                               }
                   }
                    else{
                        Log.d("updatingdevicesinfo","erroe while update device info in all_losts")
                       Toast.makeText(requireActivity(),R.string.post_updated_error, Toast.LENGTH_SHORT).show()
                       binding.updatePostProgressbar.visibility = View.GONE

                   }
                }

    }


    /**@author
     *
     */
    private fun UploadupdatedChildrensInfo(updatedname:String, updatedcolor:String, updatedtype:String,
                                         updatedplace:String, updateddate:String, mainChild:String){

        binding.updatePostProgressbar.visibility = View.VISIBLE
        // childrens child
        var childrenshashMap : HashMap<String, Any> = HashMap<String, Any> ()
        childrenshashMap.put("name_of_children", updatedname)
        childrenshashMap.put("place_of_hidden", updateddate)
        childrenshashMap.put("date_of_hidden", updatedplace)
        childrenshashMap.put("children_pirthday", updatedcolor)
        childrenshashMap.put("type", updatedtype)


        val ref = Firebase.database.reference.child("All_Losts").child(mainChild)
                .child(postId).updateChildren(childrenshashMap).addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        val ref = Firebase.database.reference.child("Users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .child("user_recorded_losts").child(mainChild)
                                .child(postId).updateChildren(childrenshashMap).addOnCompleteListener{task ->
                                    if (task.isSuccessful){
                                        Toast.makeText(requireActivity(),R.string.post_updated, Toast.LENGTH_SHORT).show()
                                        binding.updatePostProgressbar.visibility = View.GONE
                                    }
                                    else{
                                        Log.d("updatingchildrensinfo","erroe while update children info in user_losts")
                                        Toast.makeText(requireActivity(),R.string.post_updated_error, Toast.LENGTH_SHORT).show()
                                        binding.updatePostProgressbar.visibility = View.GONE
                                    }
                                }
                    }
                    else{
                        Log.d("updatingdevicesinfo","erroe while update children info in all_losts")
                        Toast.makeText(requireActivity(),R.string.post_updated_error, Toast.LENGTH_SHORT).show()
                        binding.updatePostProgressbar.visibility = View.GONE
                    }
                }

    }

    /**@author
     *
     */

    private fun getChildrenInf(mainChild : String){
        binding.updateNameOfLost.setText(resources.getString(R.string.children_name))
        binding.updateDateOfStolen.setText(resources.getString(R.string.children_hidden_date))
        binding.updatePlaceOfStolen.setText(resources.getString(R.string.children_hidden_place))
        binding.updateColorOfStolen.setText(resources.getString(R.string.children_pirthdate))
        binding.updateStolenType.setText(resources.getString(R.string.children_type))

        val ref = Firebase.database.reference.child("All_Losts").child(mainChild)
                .child(postId).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var childrenName = snapshot.child("name_of_children").getValue(String::class.java)
                        binding.updateNameOfLost.setText(childrenName)


                        val stolendate = snapshot.child("date_of_hidden").getValue(String::class.java)
                        binding.updateDateOfStolen.setText(stolendate)


                        val stolen_place = snapshot.child("place_of_hidden").getValue(String::class.java)
                        binding.updatePlaceOfStolen.setText(stolen_place)


                        val children_pirthday = snapshot.child("children_pirthday").getValue(String::class.java)
                        binding.updateColorOfStolen.setText(children_pirthday)


                        val children_type = snapshot.child("type").getValue(String::class.java)
                        binding.updateStolenType.setText(children_type)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

    }


    /**@author
     *
     */

    private fun getDeviceInf(mainChild : String){
        val ref = Firebase.database.reference.child("All_Losts").child(mainChild)
                .child(postId).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var devicenName = snapshot.child("name_of_lost").getValue(String::class.java)
                        binding.updateNameOfLost.setText(devicenName)


                        val stolendate = snapshot.child("date_of_stolen").getValue(String::class.java)
                        binding.updateDateOfStolen.setText(stolendate)


                        val stolen_place = snapshot.child("place_of_stolen").getValue(String::class.java)
                        binding.updatePlaceOfStolen.setText(stolen_place)


                        val device_pirthday = snapshot.child("lost_color").getValue(String::class.java)
                        binding.updateColorOfStolen.setText(device_pirthday)


                        val cdevice_type = snapshot.child("type").getValue(String::class.java)
                        binding.updateStolenType.setText(cdevice_type)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

    }


    override fun onStart() {
        super.onStart()

    }




}