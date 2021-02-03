package com.i.findmythings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.i.findmythings.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    lateinit var binding : ActivityMainBinding
    lateinit var navigationController : NavController

     lateinit var appBarConfig : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        navigationController  = navHostFragment.findNavController()

        // this is to setting bottom navigation view , onclick items done aoutmatically
        binding.bottomNav.setupWithNavController(navigationController)

        bottomNavigationHidding()

        navigationController
    /*   this is to handle back icon on app bar auotmatically
        1- and give to it
        all distnations , because of all of them are hiracllty on the same activity
        2- pass the app cofigration to bottom navigation setUpWith

     */

            appBarConfig = AppBarConfiguration(
                setOf(R.id.homeFragment, R.id.userProfileFragment, R.id.notificationFragment
                    ,R.id.addLostFragment))

    }

    // this is to hide bottom navigation in some fragments
    private fun bottomNavigationHidding(){
        navigationController.addOnDestinationChangedListener{_, destination ,_ ->
            when(destination.id){
                R.id.logInFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.registerUserFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.uploadLostsFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.lostsCategoriesFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.editUserPostsFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.lostsChildrensFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.userChangingPasswordFragment -> binding.bottomNav.visibility = View.INVISIBLE
                R.id.lostDetailsragment -> binding.bottomNav.visibility = View.INVISIBLE

                else -> binding.bottomNav.visibility = View.VISIBLE
            }

        }
    }
}