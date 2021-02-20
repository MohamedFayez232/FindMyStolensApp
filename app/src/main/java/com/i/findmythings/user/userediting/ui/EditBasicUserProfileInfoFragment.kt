package com.i.findmythings.user.userediting.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentEditBasicUserProfileInfoBinding


class EditBasicUserProfileInfoFragment : Fragment() {

 lateinit var binding : FragmentEditBasicUserProfileInfoBinding

    lateinit var username : String
    lateinit var userphone : String
    lateinit var useraddress : String
    lateinit var userSecurityQuest : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_basic_user_profile_info, container, false)

        binding.updateUserinProgressbar.visibility = View.INVISIBLE

        binding.updateUserButton.setOnClickListener {
            invalidatatons()
        }



        binding.cancelChangePassIc.setOnClickListener {
            val action = EditBasicUserProfileInfoFragmentDirections.actionEditBasicUserProfileInfoFragmentToUserProfileFragment()
            findNavController().navigate(action)
        }
        return binding.root

    }



    private fun invalidatatons() {

            username = binding.userNameEditText.text.toString()
            userSecurityQuest = binding.userSecurityQuestionEdiText.text.toString()
            useraddress = binding.userAddreesEditText.text.toString()
            userphone = binding.userPhoneEditText.text.toString()



            if (TextUtils.isEmpty(username)) {
                binding.userNameEditText.setError("")
            }  else if (TextUtils.isEmpty(userSecurityQuest)) {
                binding.userSecurityQuestionEdiText.setError("")
            } else if (TextUtils.isEmpty(useraddress)) {
                binding.userAddreesEditText.setError("")
            } else if (TextUtils.isEmpty(userphone)) {
                binding.userPhoneEditText.setError("")

            }
            else {
                //  UploadInfoToFirebase(nameOfLost, dateOfLost,serialOfLost, placeOfLost,colorOfLost,vinNinmberOfLost)
                UpdateToFirebase(username, userSecurityQuest,
                    useraddress, userphone)
            }
        }




     //  UploadInfoToFirebase(nameOfLost, dateOfLost,serialOfLost, placeOfLost,colorOfLost,vinNinmberOfLost)
   private fun UpdateToFirebase(username:String, userSecurityQuest:String,
    useraddress:String, userphone:String) {
         binding.updateUserinProgressbar.visibility = View.VISIBLE


         var hashMap: HashMap<String, Any> = HashMap<String, Any>()
         hashMap.put("user_name", username)
         hashMap.put("user_phone", userphone)
         hashMap.put("user_address", useraddress)
         hashMap.put("security_quest", userSecurityQuest)

         val ref = Firebase.database.reference.child("Users")
             .child(FirebaseAuth.getInstance().currentUser!!.uid)
             .child("basic_info")

         ref.updateChildren(hashMap).addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 binding.updateUserinProgressbar.visibility = View.GONE
                 Toast.makeText(
                     requireActivity(), getString(R.string.updated_info_updated), Toast.LENGTH_SHORT
                 ).show()

             } else {
                 binding.updateUserinProgressbar.visibility = View.GONE
                 Toast.makeText(
                     requireActivity(), getString(R.string.post_updated_error), Toast.LENGTH_SHORT
                 ).show()
             }
         }

   }

    /**@author
     *
     */
    private fun getUserrData() {
        val ref = Firebase.database.reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("basic_info")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               val UserName = snapshot.child("user_name").getValue(String::class.java).toString()
                binding.userNameEditText.setText(UserName)

               val Useraddress = snapshot.child("user_address").getValue(String::class.java).toString()
                binding.userAddreesEditText.setText(Useraddress)

               val UserPhone = snapshot.child("user_phone").getValue(String::class.java).toString()
                binding.userPhoneEditText.setText(UserPhone)

               val UserSecuirtyQuestion = snapshot.child("security_quest").getValue(String::class.java).toString()
                binding.userSecurityQuestionEdiText.setText(UserSecuirtyQuestion)

                val UserEmail= snapshot.child("user_email").getValue(String::class.java).toString()
                binding.userEmailEditText.setText(UserEmail)
                binding.userEmailEditText.isEnabled = false

                val Userpass= snapshot.child("user_password").getValue(String::class.java).toString()
                binding.userPassEditText.setText(Userpass)
                binding.userPassEditText.isEnabled = false
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }



override fun onStart() {
    super.onStart()
    getUserrData()
}
}