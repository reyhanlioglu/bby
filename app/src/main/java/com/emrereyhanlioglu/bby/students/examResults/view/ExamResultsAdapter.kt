package com.emrereyhanlioglu.bby.students.examResults.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.databinding.RowExamResultBinding
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult

class ExamResultsAdapter(val resultList: ArrayList<ExamResult>) : RecyclerView.Adapter<ExamResultsAdapter.ExamResultsViewHolder>(), ExamResultClickListener{


    fun updateExamResultList(newExamResults: List<ExamResult>) {
        resultList.clear()
        resultList.addAll(newExamResults)
        notifyDataSetChanged()
        println("UPDATE CALLED")
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamResultsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
       // val view = inflater.inflate(R.layout.row_exam_result, parent, false) //BEFORE DATA BINDING

        // INFLATE FROM DATA BINDING
        val view = DataBindingUtil.inflate<RowExamResultBinding>(inflater, R.layout.row_exam_result, parent, false)
        return ExamResultsViewHolder(view)
    }

    override fun getItemCount() = resultList.size


    override fun onBindViewHolder(holder: ExamResultsViewHolder, position: Int) {
        //SETUP DATA
       // holder.view.name = resultList[position].name

        holder.binding.examResult = resultList[position] //binding is coming from class ExamResultViewHolder parameter

    }


    override fun onExamResultClicked(v: View) {
       // val id = v.movieId.text.toString().toInt()
    }


    class ExamResultsViewHolder(var binding: RowExamResultBinding) : RecyclerView.ViewHolder(binding.root)
}