package com.emrereyhanlioglu.bby.students.remainingTime


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emrereyhanlioglu.bby.R
import kotlinx.android.synthetic.main.fragment_remaining_time.*
import java.text.SimpleDateFormat
import java.util.*


class RemainingTimeFragment : Fragment() {

    val TYT_DATE = SimpleDateFormat("dd-MM-yyyy-hh").parse("13-06-2020-10")
    val date2 = SimpleDateFormat("dd-MM-yyyy").parse("14-06-2020")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_remaining_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculateRemainingTime()
    }



    private fun calculateRemainingTime() {
        val currentDate = Date()

        var totalMinutes = (TYT_DATE.time - currentDate.time)/(1000*60) //Minutes
        var minutes = totalMinutes%60
        var hours = (totalMinutes/60)%24
        var days  = (totalMinutes/60)/24

      //  remainingTime.text =diff.toString()+ "Gün"
        remainingDays.text = days.toInt().toString() + " GÜN"
        remainingHours.text = hours.toInt().toString() + " SAAT"
        remainingMinutes.text = minutes.toInt().toString() + " DAKİKA"
    }


}
