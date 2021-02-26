package com.i.findmythings.categories.devices.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.categories.devices.model.CategoriesrecyclerModel
import com.i.findmythings.categories.devices.model.LostsCategoryViewHolder
import com.i.findmythings.categories.devices.ui.CategoriesViewModel
import com.i.findmythings.categories.devices.ui.LostsCategoriesFragmentArgs
import com.i.findmythings.databinding.FragmentLostsCategoriesBinding


class LostsCategoriesFragment : Fragment() {

    lateinit var binding : FragmentLostsCategoriesBinding
    lateinit var viewModel : CategoriesViewModel
    val args : LostsCategoriesFragmentArgs by navArgs()
    lateinit var MainChild: String

    lateinit var layoutManager : RecyclerView.LayoutManager
    private  var posirion = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // inflate layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_losts_categories,
                container, false)


        // view Model
        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)




        // to handel back to home fragment

        binding.backIamgeCategoriesMain.setOnClickListener {
            val action = LostsCategoriesFragmentDirections.actionLostsCategoriesFragmentToHomeFragment()
            findNavController().navigate(action)
        }

         binding.progressBarCategories.visibility = View.INVISIBLE
         posirion = args.recyclerClivkedItemPosition

        validateEditText()

        checkPositionValidations()

        return binding.root

    }

    private fun validateEditText(){
        binding.serachEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (!s.toString().isEmpty()) {
                    DatabaseChildCategorySearch(s.toString())
                } else {
                    DatabaseChildCategorySearch("")
                }
            }
        })
    }

    private fun checkPositionValidations(){
        if (posirion==0){
            MainChild = "LabTobs"
            binding.serachEditText.hint = resources.getString(R.string.enter_serial_number)

        }
        else if(posirion==1){
            MainChild = "Phones"
            binding.serachEditText.hint = resources.getString(R.string.enter_serial_number)
        }
        else if(posirion == 2){
            MainChild = "Cars_Vichels"
            binding.serachEditText.hint= (resources.getString(R.string.enter_motor_number))
        }

    }


    /**
     *
     */
    private fun DatabaseChildCategorySearch(userText: String){
        binding.progressBarCategories.visibility = View.VISIBLE

        Log.d("argsGetteings", "position is:" + MainChild)
        layoutManager = LinearLayoutManager(requireActivity())
        binding.lostsRecyclerView.layoutManager= layoutManager

        val rootRef = Firebase.database.reference.child("All_Losts")

        var query: Query = rootRef.child(MainChild)
                .orderByChild("serial_number_of_lost").limitToFirst(2)
                .startAt(userText).endAt(userText + "\uf8ff")


        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBarCategories.visibility = View.GONE
                    binding.deviceNotFoundedTextCategories.visibility = View.INVISIBLE

                }
                else{
                    binding.progressBarCategories.visibility = View.GONE
                    binding.deviceNotFoundedTextCategories.visibility = View.VISIBLE
                    binding.deviceNotFoundedTextCategories.setText(getString(R.string.no_device_text))
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
                LostsCategoryViewHolder>(firebaseRecyclerOptions) {
            override fun onBindViewHolder(
                viewHolder: LostsCategoryViewHolder,
                i: Int,
                model: CategoriesrecyclerModel) {
                viewHolder.setLostName(model.name_of_lost)
                viewHolder.setLostImageLogo(model.lost_logo_image_uri, requireActivity())
                viewHolder.itemView.setOnClickListener(View.OnClickListener { //Toast.makeText(getContext(), model.getUserName(), Toast.LENGTH_SHORT).show();
                    // navigation here
                    Toast.makeText(requireActivity(),
                        "id is :"+model.serial_number_of_lost,Toast.LENGTH_SHORT).show()
                    val action = LostsCategoriesFragmentDirections.
                    actionLostsCategoriesFragmentToLostDetailsragment(model.serial_number_of_lost,
                            MainChild, model.publisher_id)
                    findNavController().navigate(action)
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LostsCategoryViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.losts_recycler_layout, parent, false)
                return LostsCategoryViewHolder(view)
            }


        }
        firebaseRecyclerAdapter.startListening()
        binding.lostsRecyclerView.setAdapter(firebaseRecyclerAdapter)
    }





}