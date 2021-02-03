package com.i.findmythings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.i.findmythings.databinding.FragmentSplashragmentBinding


class Splashragment : Fragment() {

    lateinit var binding: FragmentSplashragmentBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splashragment, container, false)


        val CurrentUser = Firebase.auth.currentUser

        if(CurrentUser != null){
            val action = SplashragmentDirections.actionSplashragmentToHomeFragment()
            findNavController().navigate(action)
        }
        else{
            //send to login any string argument it will not be used in anything , any string u want send
            val action = SplashragmentDirections.actionSplashragmentToLogInFragment("anyString")
            findNavController().navigate(action)
        }







        return binding.root

    }


}