package com.emrereyhanlioglu.bby.volunteers.attendanceShare


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.volunteers.examResultShare.ExamResultShareFragmentArgs
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_attendance_share.*
import kotlinx.android.synthetic.main.fragment_attendance_share.view.*

class AttendanceShareFragment : Fragment() {

    private var selectedMonth: String = ""
    private var selectedAttendanceType: String = ""

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
        return inflater.inflate(R.layout.fragment_attendance_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.attendanceShareStudentUid.text = studentUid
        view.attendanceShareStudentClass.text = studentClass
        view.attendanceShareStudentName.text = studentName
        view.attendanceShareWriter.text = FirebaseAuth.getInstance().currentUser?.uid.toString()

        setupMonthsSpinner()
        setupAttendanceTypeSpinner()
    }



    private fun setupMonthsSpinner() {
        val months = arrayListOf("Bir ay seçiniz", "Eylül", "Ekim","Kasım", "Aralık", "Ocak", "Şubat", "Mart", "Nisan", "Mayıs")
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            context!!, R.layout.support_simple_spinner_dropdown_item, months
        ) {
            override fun isEnabled(position: Int): Boolean {
                return (position != 0)
            }

            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView



                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(resources.getColor(R.color.colorAccent))

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                return view
            }
        }

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerMonths.adapter = spinnerArrayAdapter

    }


    private fun setupAttendanceTypeSpinner() {
        val months = arrayListOf("Ders", "Soru Çözümü")
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
            context!!, R.layout.support_simple_spinner_dropdown_item, months
        ) {


            override fun getDropDownView(
                position: Int, convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView

                tv.setTextColor(resources.getColor(R.color.colorAccent))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                return view
            }
        }

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerAttendanceType.adapter = spinnerArrayAdapter
        spinnerAttendanceType.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAttendanceType = parent?.getItemAtPosition(position).toString()
            }

        }
    }

}
