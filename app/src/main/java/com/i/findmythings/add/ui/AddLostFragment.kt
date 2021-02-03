package com.i.findmythings.add.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

import androidx.navigation.fragment.findNavController
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentAddLostBinding


class AddLostFragment : Fragment() , AdapterView.OnItemSelectedListener{

    lateinit var binding : FragmentAddLostBinding
    lateinit var MainChild : String
    lateinit var viewModel : AddLostsViewModel

    lateinit var nameOfLost : String
    lateinit var dateOfLost : String
    lateinit var placeOfLost : String
    lateinit var serialOfLost : String
    lateinit var colorOfLost : String
    lateinit var type : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // inflate layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_lost,
            container, false)

        viewModel = ViewModelProviders.of(this).get(AddLostsViewModel::class.java)
         

        // this for spinner setting up
        val adapter = ArrayAdapter.createFromResource(requireActivity(),
            R.array.numbers, R.layout.support_simple_spinner_dropdown_item
        ) as ArrayAdapter
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.spinner1.adapter = adapter
        binding.spinner1.onItemSelectedListener = this


        binding.sendInfoButton.isEnabled = false
        binding.sendInfoButton.isClickable = false

        binding.sendInfoButton.setOnClickListener{View ->
            invalidatatons()
        }

        binding.spinnerChoosenText.visibility = View.INVISIBLE

        binding.fragmentNameToolbar.text = resources.getString(R.string.add_post_fragmentName)

        return binding.root

    }



    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
      if(position == 0){

      }
        else if(position==1){
          isSpinnerSelectedPositionOne()
        }
      else if(position==2){
          isSpinnerSelectedPositionTwo()
      }
      else if(position==3){
          isSpinnerSelectedPositionThree()
      }
      else if(position==4){
          isSpinnerSelectedPositionFour()
      }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // say somthing here
        Toast.makeText(requireActivity(),R.string.validate_spinner, Toast.LENGTH_SHORT).show()
    }

    private fun isSpinnerSelectedPositionOne(){
        binding.spinnerChoosenText.visibility = View.VISIBLE
        binding.spinnerChoosenText.setText(resources.getString(R.string.spinner_phones))
        binding.sendInfoButton.isEnabled = true
        binding.sendInfoButton.isClickable = true

        MainChild = "Phones"
        binding.colorOfLostTextHeader.setText(resources.getString(R.string.color_ofStolen_lost_thing))
        binding.nameOfLostTextHeader.setText(resources.getString(R.string.name_of_lost_thing))
        binding.serialOfLostTextHeader.setText(resources.getString(R.string.serialNumer_of_lost_thing))
        binding.dateOfLostTextHeader.setText(resources.getString(R.string.date_ofStolen_lost_thing))
        binding.placeOfLostTextHeader.setText(resources.getString(R.string.place_ofStolen_lost_thing))
        binding.typeOfLostTextHeader.setText(resources.getString(R.string.type))




    }

    private fun isSpinnerSelectedPositionTwo(){
        binding.spinnerChoosenText.visibility = View.VISIBLE
        binding.spinnerChoosenText.setText(resources.getString(R.string.spinner_labtobs))
        binding.sendInfoButton.isEnabled = true
        binding.sendInfoButton.isClickable = true
        MainChild = "LabTobs"
        binding.colorOfLostTextHeader.setText(resources.getString(R.string.color_ofStolen_lost_thing))
        binding.nameOfLostTextHeader.setText(resources.getString(R.string.name_of_lost_thing))
        binding.serialOfLostTextHeader.setText(resources.getString(R.string.enter_serial_number))
        binding.dateOfLostTextHeader.setText(resources.getString(R.string.date_ofStolen_lost_thing))
        binding.placeOfLostTextHeader.setText(resources.getString(R.string.place_ofStolen_lost_thing))
        binding.typeOfLostTextHeader.setText(resources.getString(R.string.type))





    }

    private fun isSpinnerSelectedPositionThree(){
        binding.spinnerChoosenText.visibility = View.VISIBLE
        binding.spinnerChoosenText.setText(resources.getString(R.string.spinner_childrens))
        binding.sendInfoButton.isEnabled = true
        binding.sendInfoButton.isClickable = true

        MainChild = "Childrens"
        binding.colorOfLostTextHeader.setText(resources.getString(R.string.children_pirthdate))
        binding.nameOfLostTextHeader.setText(resources.getString(R.string.children_name))
        binding.serialOfLostTextHeader.setText(resources.getString(R.string.children_nationa_id))
        binding.dateOfLostTextHeader.setText(resources.getString(R.string.children_hidden_date))
        binding.placeOfLostTextHeader.setText(resources.getString(R.string.children_hidden_place))
        binding.typeOfLostTextHeader.setText(resources.getString(R.string.children_type))

    }


    private fun isSpinnerSelectedPositionFour(){
        binding.spinnerChoosenText.visibility = View.VISIBLE
        binding.spinnerChoosenText.setText(resources.getString(R.string.spinner_cars_vichels))
        binding.sendInfoButton.isEnabled = true
        binding.sendInfoButton.isClickable = true

        MainChild = "Cars_Vichels"
        binding.colorOfLostTextHeader.setText(resources.getString(R.string.color_ofStolen_lost_thing))
        binding.nameOfLostTextHeader.setText(resources.getString(R.string.name_of_lost_thing))
        binding.serialOfLostTextHeader.setText(resources.getString(R.string.enter_motor_number))
        binding.dateOfLostTextHeader.setText(resources.getString(R.string.date_ofStolen_lost_thing))
        binding.placeOfLostTextHeader.setText(resources.getString(R.string.place_ofStolen_lost_thing))
        binding.typeOfLostTextHeader.setText(resources.getString(R.string.type))


    }

    private fun invalidatatons() {
        if (MainChild.equals("Cars_Vichels") or MainChild.equals("Childrens") or  MainChild.equals("LabTobs") or
                MainChild.equals("Phones")) {
            nameOfLost = binding.nameOfLost.text.toString()
            serialOfLost = binding.serialNumberOfLost.text.toString()
            dateOfLost = binding.dateOfStolen.text.toString()
            placeOfLost = binding.placeOfStolen.text.toString()
            colorOfLost = binding.colorOfStolen.text.toString()
            type = binding.stolenType.text.toString()


            if (TextUtils.isEmpty(nameOfLost)) {
                binding.nameOfLost.setError("")
            } else if (TextUtils.isEmpty(serialOfLost)) {
                binding.serialNumberOfLost.setError("")
            } else if (TextUtils.isEmpty(dateOfLost)) {
                binding.dateOfStolen.setError("")
            } else if (TextUtils.isEmpty(placeOfLost)) {
                binding.placeOfStolen.setError("")
            } else if (TextUtils.isEmpty(colorOfLost)) {
                binding.colorOfStolen.setError("")
            } else {
                //  UploadInfoToFirebase(nameOfLost, dateOfLost,serialOfLost, placeOfLost,colorOfLost,vinNinmberOfLost)
                val action = AddLostFragmentDirections.actionAddLostFragmentToUploadLostsFragment(
                        nameOfLost, serialOfLost, colorOfLost, type,
                        placeOfLost, dateOfLost, MainChild)
                findNavController().navigate(action)
            }
        }
        else{
            // user dosent choose a category
            Toast.makeText(requireActivity(),R.string.validate_spinner, Toast.LENGTH_SHORT).show()

        }
    }

}