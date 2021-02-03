package com.i.findmythings.user.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.categories.model.*
import com.i.findmythings.databinding.FragmentUserProfileBinding


class UserProfileFragment : Fragment() {

    lateinit var binding: FragmentUserProfileBinding

    private lateinit var UserName: String
    private lateinit var Useraddress: String
    private lateinit var UserEmail: String
    private lateinit var UserPhone: String

    lateinit var mauth : FirebaseAuth


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        binding.usernameLayout.visibility = View.GONE

        getUserChildrensReports()
        getUserDevicesReports()

        mauth = Firebase.auth

         binding.logOutText.setOnClickListener {
             userSignOut()
         }

        setUpNavigations()

        binding.detailsTextProfile.setOnClickListener {

            val action = UserProfileFragmentDirections.actionUserProfileFragmentToEditBasicUserProfileInfoFragment()
            findNavController().navigate(action)
        }



        return binding.root
    }

    private fun setUpNavigations() {
        binding.backImageToolbar.setOnClickListener {
            val action = UserProfileFragmentDirections.actionUserProfileFragmentToHomeFragment()
            findNavController().navigate(action)


        }
        binding.fragmentNameToolbar.text = resources.getString(R.string.userProfile_fragment)



    }

    private fun getUserChildrensReports() {
        binding.childrensCategoryProgressbar.visibility = View.VISIBLE
        val MainChild: String = "Childrens"
        val layoutManager: RecyclerView.LayoutManager

        layoutManager = LinearLayoutManager(requireActivity())
        binding.userProfileChildrensRecycler.layoutManager = layoutManager

        val rootRef = Firebase.database.reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)

        var query: Query = rootRef.child("user_recorded_losts")
                .child(MainChild).orderByChild("name_of_children")


        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.childrensCategoryProgressbar.visibility = View.GONE
                    binding.noChildrensReposrtsTextProfile.visibility = View.INVISIBLE
                } else {
                    binding.childrensCategoryProgressbar.visibility = View.GONE
                    binding.noChildrensReposrtsTextProfile.visibility = View.VISIBLE
                    binding.noChildrensReposrtsTextProfile.setText(getString(R.string.no_reports))
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        var firebaseRecyclerOptions: FirebaseRecyclerOptions<LostsChildrenRecyclerModel> =
                FirebaseRecyclerOptions.Builder<LostsChildrenRecyclerModel>()
                        .setQuery(query, LostsChildrenRecyclerModel::class.java)
                        .build()


        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<LostsChildrenRecyclerModel,
                UserProfileChildrensReportsViewHolder>(firebaseRecyclerOptions) {
            override fun onBindViewHolder(
                    viewHolder: UserProfileChildrensReportsViewHolder,
                    i: Int,
                    model: LostsChildrenRecyclerModel) {
                viewHolder.setLostName(model.name_of_children)
                viewHolder.setLostPlace(model.place_of_hidden)
                viewHolder.setLostImageLogo(model.lost_logo_image_uri, requireActivity())
                viewHolder.itemView.setOnClickListener(View.OnClickListener {
                    //Toast.makeText(getContext(), model.getUserName(), Toast.LENGTH_SHORT).show();

                    val action = UserProfileFragmentDirections
                            .actionUserProfileFragmentToLostDetailsragment(model.children_nationa_id,
                                    MainChild, FirebaseAuth.getInstance().currentUser!!.uid)
                    findNavController().navigate(action)
                })

                // edit post
                viewHolder.editPost.setOnClickListener {
                    val action = UserProfileFragmentDirections.actionUserProfileFragmentToEditUserPostsFragment(
                            model.children_nationa_id, MainChild)
                    findNavController().navigate(action)
                }

                // delet post
                viewHolder.deletePost.setOnClickListener {

                    val ref = Firebase.database.reference.child("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("user_recorded_losts")
                              .child("Childrens")
                               .child(model.children_nationa_id).removeValue().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val ref = Firebase.database.reference.child("All_Losts")
                                            .child(MainChild)
                                            .child(model.children_nationa_id).removeValue()
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val ref = Firebase.database.reference.child("lost_images")
                                                            .child(model.children_nationa_id).removeValue()
                                                            .addOnCompleteListener { task->
                                                                if (task.isSuccessful){
                                                                    Toast.makeText(requireActivity(),
                                                                            R.string.items_removed, Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                } else {
                                                    Toast.makeText(requireActivity(),
                                                            R.string.items_removed_error2, Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                } else {
                                    Toast.makeText(requireActivity(),
                                            R.string.items_removed_error, Toast.LENGTH_SHORT).show()
                                }
                            }
                }

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileChildrensReportsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_posts_recycler_layout, parent, false)
                return UserProfileChildrensReportsViewHolder(view)
            }

        }



    firebaseRecyclerAdapter.startListening()
    binding.userProfileChildrensRecycler.setAdapter(firebaseRecyclerAdapter)


    }



    private fun getUserDevicesReports(){
        binding.devicesCategoryProgressbar.visibility = View.VISIBLE

        val MainChild ="devices_reports"

        val layoutManager : RecyclerView.LayoutManager

        layoutManager = LinearLayoutManager(requireActivity())
        binding.userProfileDevicesRecycler.layoutManager= layoutManager

        val rootRef = Firebase.database.reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val query: Query = rootRef.child("user_recorded_losts")
                .child(MainChild)
                .orderByChild("serial_number_of_lost")



        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.devicesCategoryProgressbar.visibility = View.GONE
                    binding.devicesCategoryProgressbar.visibility = View.INVISIBLE

                } else {
                    binding.devicesCategoryProgressbar.visibility = View.GONE
                    binding.noDevicesReposrtsTextProfile.visibility = View.VISIBLE
                    binding.noDevicesReposrtsTextProfile.setText(getString(R.string.no_reports))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        val firebaseRecyclerOptions: FirebaseRecyclerOptions<CategoriesrecyclerModel> =
                FirebaseRecyclerOptions.Builder<CategoriesrecyclerModel>()
                        .setQuery(query, CategoriesrecyclerModel::class.java)
                        .build()

        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CategoriesrecyclerModel,
                UserProfilDevicesReportsViewHolder>(firebaseRecyclerOptions) {
            override fun onBindViewHolder(
                    viewHolder: UserProfilDevicesReportsViewHolder,
                    i: Int,
                    model: CategoriesrecyclerModel) {
                viewHolder.setLostName(model.name_of_lost)
                viewHolder.setLostImageLogo(model.lost_logo_image_uri, requireActivity())
                viewHolder.itemView.setOnClickListener(View.OnClickListener
                {
                    val action = UserProfileFragmentDirections
                            .actionUserProfileFragmentToLostDetailsragment(model.serial_number_of_lost,
                                    MainChild, FirebaseAuth.getInstance().currentUser!!.uid)
                    findNavController().navigate(action)
                })
                val category = model.category
                // edit post
                viewHolder.editPost.setOnClickListener {
                    val action = UserProfileFragmentDirections.actionUserProfileFragmentToEditUserPostsFragment(
                            model.serial_number_of_lost, category)
                     findNavController().navigate(action)

                }


                // delet post
                viewHolder.deletePost.setOnClickListener {
                  val ref = Firebase.database.reference.child("Users")
                          .child(FirebaseAuth.getInstance().currentUser!!.uid).child("user_recorded_losts")
                          .child("devices_reports")
                          .child(model.serial_number_of_lost).removeValue().addOnCompleteListener{ task ->
                              if (task.isSuccessful){
                                  val ref = Firebase.database.reference.child("All_Losts")
                                     .child(category)
                                      .child(model.serial_number_of_lost).removeValue()
                                          .addOnCompleteListener{ task ->
                                              if (task.isSuccessful){
                                               val ref = Firebase.database.reference.child("lost_images")
                                                  .child(model.serial_number_of_lost).removeValue()
                                                    .addOnCompleteListener { task->
                                                       if (task.isSuccessful){
                                                               Toast.makeText(requireActivity(),
                                                                       R.string.items_removed, Toast.LENGTH_SHORT).show()
                                                           }
                                                       }
                                              }
                                              else{
                                                  Toast.makeText(requireActivity(),
                                                          R.string.items_removed_error2, Toast.LENGTH_SHORT).show()
                                              }
                                          }
                              }
                              else{
                                  Toast.makeText(requireActivity(),
                                          R.string.items_removed_error, Toast.LENGTH_SHORT).show()
                              }
                          }

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfilDevicesReportsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.user_posts_recycler_layout, parent, false)
                return UserProfilDevicesReportsViewHolder(view)
            }


        }
        firebaseRecyclerAdapter.startListening()
        binding.userProfileDevicesRecycler.setAdapter(firebaseRecyclerAdapter)
    }



    private fun userSignOut(){
        val user = mauth.currentUser
        if (user != null) {
            mauth.signOut()

            // send to login the current user uid to use there in changing basword after sign out if user want
            val action  = UserProfileFragmentDirections.actionUserProfileFragmentToLogInFragment(user.uid)
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireActivity(), resources.getString(R.string.must_logged_first), Toast.LENGTH_SHORT).show()
        }



    }



    }



