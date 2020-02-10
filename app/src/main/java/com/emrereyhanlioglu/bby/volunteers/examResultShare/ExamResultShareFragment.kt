package com.emrereyhanlioglu.bby.volunteers.examResultShare


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_exam_result_share.view.*


class ExamResultShareFragment : Fragment() {
    private var studentUid: String? = null
    private var studentClass: String? = null
    private var studentName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentUid = ExamResultShareFragmentArgs.fromBundle(it).studentUid
            studentClass = ExamResultShareFragmentArgs.fromBundle(it).className
            studentName = ExamResultShareFragmentArgs.fromBundle(it).studentName
        }



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.emrereyhanlioglu.bby.R.layout.fragment_exam_result_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.examResultStudentUid.text = studentUid
        view.examResultStudentClass.text = studentClass
        view.examResultStudentName.text = studentName
    }



}
