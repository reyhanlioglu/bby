package com.emrereyhanlioglu.bby.volunteers.examResultShare


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.volunteers.examResultShare.model.Student
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_select_student.*
import kotlinx.android.synthetic.main.row_student_list.view.*


class SelectStudentFragment : Fragment() {
    private lateinit var className: String
    private lateinit var destinationPage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            className = SelectStudentFragmentArgs.fromBundle(it).selectedClass
            destinationPage = SelectClassFragmentArgs.fromBundle(it).destinationPage
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_student, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStudentNameList()

    }


    private fun getStudentNameList() {
        /*
        // OLD FIREBASE REALTIME DATABASE CODES
        val ref = FirebaseDatabase.getInstance().getReference("Users/Students/$className")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()


                p0.children.forEach {
                    val fullname = it.child("fullname").getValue(String::class.java)
                    val uid = it.child("uid").getValue(String::class.java)
                    fullname?.let { fname ->
                        uid?.let { uid ->
                            adapter.add(
                                StudentNameItem(
                                    Student(fname, uid, className), destinationPage
                                )
                            )
                        }
                    }
                }
                studentList?.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

         */

        val newRef = FirebaseFirestore.getInstance().collection("Users/Students/$className").get()

            .addOnSuccessListener { students->
                val adapter = GroupAdapter<ViewHolder>()
                for(student in students){
                    val fullname = student["fullname"]
                    val uid = student["uid"]
                    fullname?.let { fname ->
                        uid?.let { uid ->
                            adapter.add(
                                StudentNameItem(
                                    Student(fname.toString(), uid.toString(), className), destinationPage
                                )
                            )
                        }
                    }
                }
                studentList?.adapter = adapter

            }
      

    }


    class StudentNameItem(var student: Student, var destinationPage: String) : Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.row_student_list
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val item = viewHolder.itemView
            item.rowStudentName.text = student.fullname
            item.rowStudentInfo.setOnClickListener {

                when(destinationPage) {
                    "exam result" -> {
                        val action = SelectStudentFragmentDirections.actionExamResultShare()
                        action.studentName = student.fullname
                        action.studentUid = student.uid
                        action.className = student.className
                        Navigation.findNavController(it).navigate(action)
                    }

                    "attendance" -> {
                        val action = SelectStudentFragmentDirections.actionAttendanceShare()
                        action.studentName = student.fullname
                        action.studentUid = student.uid
                        action.className = student.className
                        Navigation.findNavController(it).navigate(action)
                    }

                    "show exam results" -> {
                        val action = SelectStudentFragmentDirections.actionShowStudentResults()
                        action.studentUid = student.uid
                        action.className = student.className
                        Navigation.findNavController(it).navigate(action)
                    }
                }

            }
        }
    }

}
