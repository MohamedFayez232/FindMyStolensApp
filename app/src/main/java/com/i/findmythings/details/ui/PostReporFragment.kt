package com.i.findmythings.details.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.internal.IResolveAccountCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.i.findmythings.R
import com.i.findmythings.databinding.FragmentPostReporBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PostReporFragment : Fragment() {

    lateinit var binding : FragmentPostReporBinding
    val args : PostReporFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
     binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_repor, container,
         false)

        val ReportedpostId = args.postId

        binding.thanksFroReportText.visibility = View.INVISIBLE
        binding.progressBarReport.visibility = View.INVISIBLE


        binding.sendReportBtn.setOnClickListener {
            validatonEditText(ReportedpostId)
        }







        return binding.root
    }

    private fun validatonEditText(ReportedPostId : String){
        val ProblemDescribtion = binding.postProblemDescribitionEditText.text.toString()
        if(TextUtils.isEmpty(ProblemDescribtion) && ProblemDescribtion.length < 20){
            binding.postProblemDescribitionEditText.setError(resources.getString(R.string.decribtion_problem_minmum))
        }
        else{
            SendReport(ReportedPostId,ProblemDescribtion)
        }
    }

    /**@author
     *
     */
    private fun SendReport(ReportedPostId : String, description:String){
        binding.progressBarReport.visibility = View.VISIBLE

        val ref = Firebase.database.reference.child("PostReports").child(ReportedPostId)

        // current date
        val s : SimpleDateFormat =
            SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        val dateTime = s.format(Date())

        val map : HashMap <String , Any> =  HashMap<String, Any>()
        map.put("report_problem_descreption" ,description)
        map.put("reporter_id" , Firebase.auth.currentUser!!.uid)
        map.put("post_id" , ReportedPostId)
        map.put("report_date" , dateTime)

        ref.updateChildren(map).addOnCompleteListener{task ->
        if(task.isSuccessful){
            binding.thanksFroReportText.text = resources.getString(R.string.thanks_for_report)
            binding.thanksFroReportText.visibility = View.VISIBLE
            binding.progressBarReport.visibility = View.GONE
            binding.sendReportBtn.isClickable = false
            binding.sendReportBtn.isEnabled = false

        }
            else{
            binding.thanksFroReportText.text = resources.getString(R.string.error_thanks_for_report)
            binding.thanksFroReportText.visibility = View.VISIBLE
            binding.progressBarReport.visibility = View.GONE
            }

        }
    }
}