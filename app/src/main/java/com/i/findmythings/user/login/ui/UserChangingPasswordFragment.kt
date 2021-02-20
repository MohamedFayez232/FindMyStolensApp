package com.i.findmythings.user.login.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentUserChangingPasswordBinding


class UserChangingPasswordFragment : Fragment() {

  lateinit var binding :FragmentUserChangingPasswordBinding
    lateinit var user_new_password: String
    val args : UserChangingPasswordFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_changing_password, container, false)

        binding.newUserpass.visibility = View.INVISIBLE
        binding.changePassButton.isEnabled =  false
        binding.cahngePassProgress.visibility = View.INVISIBLE
        binding.correctSecurityCodeIc.visibility = View.INVISIBLE


        validations()

       binding.cancelChangePassIc.setOnClickListener {
           val action = UserChangingPasswordFragmentDirections.actionUserChangingPasswordFragmentToLogInFragment(args.currentUserUid)
           findNavController().navigate(action)
       }

        binding.changePassButton.setOnClickListener {
            UpdateUserPassword()
        }

        binding.updatePssLoginText.setOnClickListener {
            val action = UserChangingPasswordFragmentDirections.actionUserChangingPasswordFragmentToLogInFragment(args.currentUserUid)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun validations(){

        binding.securityQuest.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(userInput: Editable?) {
               if (userInput!=null){
                   checkFromUserSeurityQuestion(userInput.toString())
               }
                else{
                   Toast.makeText(requireActivity(),resources.getString(R.string.not_text),
                   Toast.LENGTH_SHORT).show()
                }
            }

        })


    }

    private fun checkFromUserSeurityQuestion(userInput:String){
        binding.cahngePassProgress.visibility = View.VISIBLE

        val ref = Firebase.database.reference.child("Users")
                .child(args.currentUserUid)
                .child("basic_info")

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val security_quest = snapshot.child("security_quest").getValue(String::class.java)
                    if (userInput.equals(security_quest)){

                        binding.newUserpass.visibility = View.VISIBLE
                        binding.changePassButton.isEnabled =  true
                        binding.cahngePassProgress.visibility = View.GONE
                        binding.correctSecurityCodeIc.visibility = View.VISIBLE

                        Toast.makeText(requireActivity(), getString(R.string.correct_security_question),
                                Toast.LENGTH_SHORT).show()

                    }
                    else{
                        binding.cahngePassProgress.visibility = View.GONE
                        Toast.makeText(requireActivity(), getString(R.string.wrong_security_question),
                                Toast.LENGTH_SHORT).show()
                    }

                }
                // this child not exists
                else{
                    Toast.makeText(requireActivity(), getString(R.string.post_updated_error), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun UpdateUserPassword(){
        binding.cahngePassProgress.visibility = View.VISIBLE

        user_new_password = binding.newUserpass.text.toString()
        if (TextUtils.isEmpty(user_new_password)){
            binding.newUserpass.setError("")
        }
        else{
            UdpatePasswordInDatabase(user_new_password)
        }
    }

    private fun UdpatePasswordInDatabase(user_new_password: String){
        val user = Firebase.auth.currentUser
        user!!.updatePassword(user_new_password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val ref = Firebase.database.reference.child("Users")
                        .child(args.currentUserUid).child("basic_info")

                var hashMap : HashMap<String, Any> = HashMap<String, Any> ()
                hashMap.put("security_quest", user_new_password)

                ref.updateChildren(hashMap).addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        binding.cahngePassProgress.visibility = View.GONE

                     Toast.makeText(requireActivity(),getString(R.string.password_changed), Toast.LENGTH_SHORT).show()
                        val action = UserChangingPasswordFragmentDirections.actionUserChangingPasswordFragmentToLogInFragment(args.currentUserUid)
                        findNavController().navigate(action)
                    }
                    else{
                        Log.d("cahngeuserpass","failed to update pass in database")
                        binding.cahngePassProgress.visibility = View.GONE
                        Toast.makeText(requireActivity(),getString(R.string.post_updated_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // update auth pass failed
            else{
                Log.d("cahngeuserpass","failed to update auth pass")
                binding.cahngePassProgress.visibility = View.GONE
                Toast.makeText(requireActivity(),getString(R.string.post_updated_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}