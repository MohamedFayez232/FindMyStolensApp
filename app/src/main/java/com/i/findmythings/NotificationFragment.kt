package com.i.findmythings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.i.findmythings.databinding.FragmentNotificationBinding
import com.i.findmythings.user.profile.ui.UserProfileFragmentDirections


class NotificationFragment : Fragment() {


    lateinit var binding : FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification,
            container, false)

        setUpNavigations()
        return binding.root
    }


    private fun setUpNavigations() {
        binding.backImageNotification.setOnClickListener {
            val action = NotificationFragmentDirections.actionNotificationFragmentToHomeFragment()
            findNavController().navigate(action)


        }




    }

}