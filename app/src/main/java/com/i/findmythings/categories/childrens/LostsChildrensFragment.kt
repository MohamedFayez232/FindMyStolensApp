package com.i.findmythings.categories.childrens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.categories.devices.model.ChildrensLostRecyclerViewHolder
import com.i.findmythings.categories.devices.model.LostsChildrenRecyclerModel
import com.i.findmythings.databinding.FragmentLostsChildrensBinding



class LostsChildrensFragment : Fragment() {

    lateinit var binding : FragmentLostsChildrensBinding

    lateinit var layoutManager : RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // inflate layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_losts_childrens,
            container, false)

        binding.progressBarChildrensRecycler.visibility = View.INVISIBLE

        SearcheditTextListner()

        binding.backIamgeToMainFragmrnt.setOnClickListener {
            val action = LostsChildrensFragmentDirections.actionLostsChildrensFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    /**@author
     *
     */
    private fun SearcheditTextListner() {
        binding.textInputEditTextSearcChildrens.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(userInput: Editable?) {
                if (!userInput.toString().isEmpty()) {
                    getUserSearches(userInput.toString())
                } else {
                    getUserSearches("")
                }
            }

        })
    }

    /**@author
     *
     */
    private fun getUserSearches(userInput : String){
        binding.progressBarChildrensRecycler.visibility = View.VISIBLE
        val MainChild :String = "Childrens"
        layoutManager = LinearLayoutManager(requireActivity())
        binding.childrenLostsRecycler.layoutManager= layoutManager

        val rootRef = Firebase.database.reference.child("All_Losts").child(MainChild)
        var query: Query = rootRef.orderByChild("name_of_children")
                .startAt(userInput).endAt(userInput + "\uf8ff")

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.progressBarChildrensRecycler.visibility = View.GONE
                    binding.childrenFoundedChildrenRecyclerText.visibility = View.INVISIBLE
                }
                else{
                    binding.progressBarChildrensRecycler.visibility = View.GONE
                    binding.childrenFoundedChildrenRecyclerText.visibility = View.VISIBLE
                    binding.childrenFoundedChildrenRecyclerText.setText(getString(R.string.no_device_text))
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
                ChildrensLostRecyclerViewHolder>(firebaseRecyclerOptions) {
            override fun onBindViewHolder(
                    viewHolder: ChildrensLostRecyclerViewHolder,
                    i: Int,
                    model: LostsChildrenRecyclerModel) {
                viewHolder.setLostName(model.name_of_children)
                viewHolder.setLostPlace(model.place_of_hidden)
                viewHolder.setLostImageLogo(model.lost_logo_image_uri, requireActivity())
                viewHolder.itemView.setOnClickListener(View.OnClickListener {
                    //Toast.makeText(getContext(), model.getUserName(), Toast.LENGTH_SHORT).show();
                
                    val action = LostsChildrensFragmentDirections.actionLostsChildrensFragmentToLostDetailsragment(model.children_nationa_id,
                            MainChild, model.publisher_id)
                    findNavController().navigate(action)
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrensLostRecyclerViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.losts_childrens_recycler_layout, parent, false)
                return ChildrensLostRecyclerViewHolder(view)
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
        firebaseRecyclerAdapter.startListening()
        binding.childrenLostsRecycler.setAdapter(firebaseRecyclerAdapter)
    }


    /**@author
     *  when fragemnt start
     */
    private fun getChildrensData(){
        binding.progressBarChildrensRecycler.visibility = View.VISIBLE
        val MainChild :String = "Childrens"
        layoutManager = LinearLayoutManager(requireActivity())
        binding.childrenLostsRecycler.layoutManager= layoutManager

        val rootRef = Firebase.database.reference.child("All_Losts").child(MainChild)
        var query: Query = rootRef.orderByChild("name_of_children")

          query.addListenerForSingleValueEvent(object : ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {
                 if (snapshot.exists()){
                     binding.progressBarChildrensRecycler.visibility = View.GONE
                     binding.childrenFoundedChildrenRecyclerText.visibility = View.INVISIBLE
                 }
                  else{
                     binding.progressBarChildrensRecycler.visibility = View.GONE
                     binding.childrenFoundedChildrenRecyclerText.visibility = View.VISIBLE
                     binding.childrenFoundedChildrenRecyclerText.setText(getString(R.string.no_device_text))
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
                ChildrensLostRecyclerViewHolder>(firebaseRecyclerOptions) {
            override fun onBindViewHolder(
                viewHolder: ChildrensLostRecyclerViewHolder,
                i: Int,
                model: LostsChildrenRecyclerModel) {
                viewHolder.setLostName(model.name_of_children)
                viewHolder.setLostPlace(model.place_of_hidden)
                viewHolder.setLostImageLogo(model.lost_logo_image_uri, requireActivity())
                viewHolder.itemView.setOnClickListener(View.OnClickListener {
                    //Toast.makeText(getContext(), model.getUserName(), Toast.LENGTH_SHORT).show();

                    val action = LostsChildrensFragmentDirections.actionLostsChildrensFragmentToLostDetailsragment(model.children_nationa_id,
                            MainChild, model.publisher_id)
                    findNavController().navigate(action)
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrensLostRecyclerViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.losts_childrens_recycler_layout, parent, false)
                return ChildrensLostRecyclerViewHolder(view)
            }

            override fun onDataChanged() {
                super.onDataChanged()
            }
        }
        firebaseRecyclerAdapter.startListening()
        binding.childrenLostsRecycler.setAdapter(firebaseRecyclerAdapter)
    }

    override fun onStart() {
        super.onStart()
        getChildrensData()
    }

}

