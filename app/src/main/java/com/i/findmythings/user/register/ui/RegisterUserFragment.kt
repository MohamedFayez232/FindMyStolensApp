package com.i.findmythings.user.register.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentRegisterUserBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap




class RegisterUserFragment : Fragment() {
    lateinit var binding : FragmentRegisterUserBinding
    val args : RegisterUserFragmentArgs by navArgs()
    lateinit var username : String
    lateinit var userphone : String
    lateinit var userpassword : String
    lateinit var useremail : String
    lateinit var useraddress : String
    lateinit var securityQuestion : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflate layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_user,
            container, false)

        binding.registerProgress.visibility = View.INVISIBLE

        binding.registerButton.setOnClickListener{View ->
            invalidatatons()
        }

        binding.cancelReisterIc.setOnClickListener{View ->
           val action = RegisterUserFragmentDirections
                   .actionRegisterUserFragmentToLogInFragment(args.currenrUserId)
            findNavController().navigate(action)
        }

        binding.registerLoginText.setOnClickListener {
            val action = RegisterUserFragmentDirections
                    .actionRegisterUserFragmentToLogInFragment(args.currenrUserId)
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun invalidatatons(){
        username = binding.UsernameeditText.text.toString()
        userpassword = binding.userpass.text.toString()
        useremail = binding.emailLogin.text.toString()
        userphone = binding.UserphoneeditText.text.toString()
        useraddress = binding.UseraddresseditText.text.toString()
        securityQuestion = binding.securityQuestion.text.toString()

        if(TextUtils.isEmpty(username)){
            binding.UsernameeditText.setError("")
        }
        else if(TextUtils.isEmpty(userpassword)){
            binding.userpass.setError("")
        }
        else if(TextUtils.isEmpty(useremail) || !useremail.contains("@") || !useremail.contains(".com")){
            binding.emailLogin.setError(resources.getString(R.string.email_validations))
        }
        else if(TextUtils.isEmpty(userphone)){
            binding.UserphoneeditText.setError("")
        }
        else if(TextUtils.isEmpty(useraddress)) {
            binding.UseraddresseditText.setError("")
        }
        else if(TextUtils.isEmpty(securityQuestion) || securityQuestion.length < 5 || securityQuestion.length > 5) {
            binding.securityQuestion.setError(resources.getString(R.string.must_be_5_numbers))
        }

        else{
            UploadInfoToFirebase(username, useremail, userphone, userpassword, useraddress,securityQuestion)
        }
    }

    private fun UploadInfoToFirebase(name:String ,email:String ,phone:String ,pass:String,address:String,securityQuesttion:String ){
        binding.registerProgress.visibility = View.VISIBLE
        binding.emailLogin.isEnabled = false
        binding.userpass.isEnabled = false
        binding.UsernameeditText.isEnabled = false
        binding.UseraddresseditText.isEnabled = false
        binding.UserphoneeditText.isEnabled = false
        binding.securityQuestion.isEnabled = false
        binding.registerButton.isClickable = false


        var ref = Firebase.database.reference
        // current date
        val s : SimpleDateFormat =
        SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        val dateTime = s.format(Date())


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
            if(task.isSuccessful){
                var hashMap : HashMap<String, Any> = HashMap<String, Any> ()
                hashMap.put("user_name", name)
                hashMap.put("user_phone", phone)
                hashMap.put("user_email", email)
                hashMap.put("user_password", pass)
                hashMap.put("user_address", address)
                hashMap.put("security_quest", securityQuesttion)
                hashMap.put("user_id", Firebase.auth.currentUser!!.uid)
                hashMap.put("joining_date", dateTime)

                ref.child("Users")
                        .child(Firebase.auth.currentUser!!.uid).child("basic_info")
                        .updateChildren(hashMap)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                binding.registerProgress.visibility = View.GONE

                                Toast.makeText(requireActivity(),getString(R.string.account_created),
                                        Toast.LENGTH_SHORT).show()

                                val action = RegisterUserFragmentDirections
                                        .actionRegisterUserFragmentToHomeFragment()
                                findNavController().navigate(action)

                            }
                            else{

                                binding.registerProgress.visibility = View.GONE
                                binding.emailLogin.isEnabled = false
                                binding.userpass.isEnabled = false
                                binding.UsernameeditText.isEnabled = false
                                binding.UseraddresseditText.isEnabled = false
                                binding.UserphoneeditText.isEnabled = false
                                binding.registerButton.isClickable = false
                                Toast.makeText(requireActivity(),R.string.email_registred_before,
                                        Toast.LENGTH_SHORT).show()

                            }
                        }
            }
        }

    }



}