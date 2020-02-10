package com.emrereyhanlioglu.bby.students.examResults.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.emrereyhanlioglu.bby.BaseViewModel
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResultDatabase
import kotlinx.coroutines.launch

class ExamResultsViewModel(application: Application) : BaseViewModel(application) {

    var examResultsLiveData: MutableLiveData<MutableList<ExamResult>> = MutableLiveData()
    var examResultsLoadError = MutableLiveData<Boolean>()
    var examResultsLoading = MutableLiveData<Boolean>()

  //  private var auth: FirebaseAuth? = null
 //   private var databaseReference: DatabaseReference
  //  private var database: FirebaseDatabase? = null




 /*   init {

        examResultsLoadError.value = false
        examResultsLoading.value = false

       auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.reference.child("Users").child("Students")

     //   getExamResults()
    }
*/

    fun addResultToTheDatabase(examResult: ExamResult) {
        launch{
            val dao = ExamResultDatabase(getApplication()).examResultDao()
            dao.insertAll(examResult)
        }

    }

    fun deleteAllResultsFromDatabase(){
        launch {
            val dao = ExamResultDatabase(getApplication()).examResultDao()
            dao.deleteAll()
        }
    }





}