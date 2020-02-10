package com.emrereyhanlioglu.bby.students.statistics.model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.emrereyhanlioglu.bby.BaseViewModel
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResultDatabase
import kotlinx.coroutines.launch

class StatisticsViewModel (application: Application) : BaseViewModel(application) {

    var examResults = MutableLiveData<MutableList<ExamResult>>()

    init {
        examResults.value = mutableListOf()
    }

    fun fetchExamResultsFromDatabase(){


        launch {
            val results = ExamResultDatabase(getApplication()).examResultDao().getAllResults()
//            Toast.makeText(getApplication(), "Exam results retrieved from database!", Toast.LENGTH_LONG).show()
            examResults.value = results as MutableList<ExamResult>
            println("fetchExamResultsFromDatabase called!")
        }




            // update views
          //country.updateCaption(countryName)
          //  region.updateCaption(regionName)



    }
}