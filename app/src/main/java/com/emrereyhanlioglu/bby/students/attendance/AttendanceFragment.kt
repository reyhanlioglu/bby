package com.emrereyhanlioglu.bby.students.attendance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.students.attendance.model.AttendanceDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_attendance.*


class AttendanceFragment : Fragment() {
    private lateinit var prefHelper: SharedPreferencesHelper
    private var auth: FirebaseAuth? = null

    val header : MutableList<String> = ArrayList()

    private var attendanceAll: MutableList<MutableList<AttendanceDetail?>?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        attendanceAll = mutableListOf()
        prefHelper = SharedPreferencesHelper(activity!!.applicationContext)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //addMonths()

        fetchAttendances()
    }




    private fun addMonths() {
        header.add("Eylül")
        header.add("Ekim")
        header.add("Kasım")
        header.add("Aralık")
        header.add("Ocak")
        header.add("Şubat")
        header.add("Mart")
        header.add("Nisan")
        header.add("Mayıs")
    }

    private fun fetchAttendances() {
        val currentUserUid = auth!!.currentUser?.uid
        val className = prefHelper.getClassName()

        val newRef = FirebaseFirestore.getInstance().collection("Attendance/$className/User-$currentUserUid")
            .get().addOnSuccessListener { attendance->
                println("INSIDE: ${attendance.size()}")
                for(month in attendance.documents){
                    println("Month: $month")
                    val tempList: MutableList<AttendanceDetail?> = mutableListOf()
                   /* for(attendance in month.data){
                        println("Attendance record: $attendance")
                        val detailRecords = attendance.value as AttendanceDetail
                        println("Detail record is $detailRecords")
                    }

                    */
                }
            }.addOnFailureListener{
                println("Failed")
            }

        val ref = FirebaseDatabase.getInstance().getReference("/Users/Students/$className/User-$currentUserUid/attendance")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
           //     val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach { month ->
                  //  val monthName = p1.getValue(String::class.java)
                    var currentMonth: String = ""

                    val tempList: MutableList<AttendanceDetail?> = mutableListOf()
                    month.children.forEach { details ->
                        val detailRecords = details.getValue(AttendanceDetail::class.java)
                        tempList.add(detailRecords)
                        currentMonth = detailRecords?.month!!
                    }

                    // Add Month Header to the ListView
                    if(!header.equals("") && !header.contains(currentMonth)){
                        header.add(currentMonth)
                    }
                    attendanceAll?.add(tempList)

                }

                expandableListView?.setAdapter(ExpandableListAdapter(context,expandableListView, header, attendanceAll))

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
