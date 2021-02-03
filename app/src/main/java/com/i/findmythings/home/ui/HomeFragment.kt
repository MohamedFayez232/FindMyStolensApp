package com.i.findmythings.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentHomeBinding
import com.i.findmythings.home.model.MainCategoryModel
import com.i.findmythings.home.model.MainCategoryRecyclerAdapter
import kotlinx.coroutines.NonCancellable.children

class HomeFragment : Fragment(), MainCategoryRecyclerAdapter.RecyclerViewClickListener {

    lateinit var binding : FragmentHomeBinding
    lateinit var catgoryList : ArrayList<MainCategoryModel>
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : MainCategoryRecyclerAdapter

            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, com.i.findmythings.R.layout.fragment_home,
            container, false)

           binding.serachToolbarHomeIc.setOnClickListener {
               val action = HomeFragmentDirections.actionHomeFragmentToLostsCategoriesFragment(2)
               findNavController().navigate(action)
           }

        setUpCategoryArrayList()
        return binding.root

    }

    private fun setUpCategoryArrayList(){



         catgoryList = ArrayList<MainCategoryModel>()

        catgoryList.add(MainCategoryModel(getString(R.string.labs),R.drawable.labtops_image))
        catgoryList.add(MainCategoryModel(getString(R.string.phone),R.drawable.phone_image))
        catgoryList.add(MainCategoryModel(getString(R.string.cars),R.drawable.cars_image))
        catgoryList.add(MainCategoryModel(getString(R.string.childrens),R.drawable.childrens_image))

        layoutManager = GridLayoutManager(requireActivity(),2)
        binding.categoriesRecyclerview.layoutManager = layoutManager
        adapter = MainCategoryRecyclerAdapter(catgoryList, this,requireContext())
        binding.categoriesRecyclerview.adapter = adapter

    }




    override fun recyclerViewListClicked(v: View?, position: Int) {
        //Toast.makeText(requireContext(),"position" + position, Toast.LENGTH_SHORT).show()

        if (position == 0){
            nvitaionAnotherFragment(position)
        }
        else if(position == 1){
            nvitaionAnotherFragment(position)
        }
        else if(position == 2){
            nvitaionAnotherFragment(position)
        }
        else if(position == 3) {

          val action = HomeFragmentDirections.actionHomeFragmentToLostsChildrensFragment()
          findNavController().navigate(action)

        }

    }

    private fun nvitaionAnotherFragment(position: Int){
        val action = HomeFragmentDirections.actionHomeFragmentToLostsCategoriesFragment(position)
        findNavController().navigate(action)
    }

}