package com.i.findmythings.user.login.ui

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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentLogInBinding


class LogInFragment : Fragment() {
    lateinit var binding: FragmentLogInBinding
       val args : LogInFragmentArgs by navArgs()

    lateinit var userpassword: String
    lateinit var useremail: String
    lateinit var mauth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        // inflate layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_log_in,
            container, false
        )


        mauth = Firebase.auth


        binding.loginButton.setOnClickListener { View ->
            invalidatatons()
        }

        binding.frogotPass.setOnClickListener {
            changeUserPassword()
        }

        binding.registerLoginText.setOnClickListener { View ->
          val action = LogInFragmentDirections.actionLogInFragmentToRegisterUserFragment(args.currentUserUid)
            findNavController().navigate(action)
        }

        binding.loginProgress.visibility = View.INVISIBLE

        return binding.root
    }

    private fun invalidatatons() {
        userpassword = binding.userpass.text.toString()
        useremail = binding.emailLogin.text.toString()

        if (TextUtils.isEmpty(useremail)) {
            binding.emailLogin.setError("")

        } else if (TextUtils.isEmpty(userpassword)) {
            binding.userpass.setError("")
        }
        

        else{
            LoginUser(useremail, userpassword)
        }
    }

    private fun LoginUser(email: String, pass: String) {
        binding.loginProgress.visibility = View.VISIBLE
        binding.emailLogin.isEnabled = false
        binding.userpass.isEnabled = false
        binding.userpass.isClickable = false


        mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { task->
            if(task.isSuccessful){
                binding.loginProgress.visibility = View.GONE
                Toast.makeText(requireActivity(), getString(R.string.logged), Toast.LENGTH_SHORT).show()
                val action = LogInFragmentDirections.actionLogInFragmentToHomeFragment()
                findNavController().navigate(action)

            }

            // user auth  error
            else {
                Toast.makeText(requireActivity(),R.string.wrong_login_details,Toast.LENGTH_SHORT).show()
                binding.loginProgress.visibility = View.GONE
                binding.emailLogin.isEnabled = true
                binding.userpass.isEnabled = true
                binding.loginButton.isClickable = true
            }
        }


    }

    private fun changeUserPassword(){
        val action = LogInFragmentDirections.actionLogInFragmentToUserChangingPasswordFragment(args.currentUserUid)
        findNavController().navigate(action)
    }




}