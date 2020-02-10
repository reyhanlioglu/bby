package com.emrereyhanlioglu.bby.students.examResults.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.emrereyhanlioglu.bby.R
import com.emrereyhanlioglu.bby.SharedPreferencesHelper
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult
import com.emrereyhanlioglu.bby.students.examResults.viewmodel.ExamResultsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_exam_results.*
import kotlinx.android.synthetic.main.row_exam_result.view.*


class ExamResultsFragment : Fragment() {
    private lateinit var prefHelper: SharedPreferencesHelper

    private var auth: FirebaseAuth? = null
   // private lateinit var databaseReference: DatabaseReference
   // private var database: FirebaseDatabase? = null

    private var examResults: ArrayList<ExamResult>? = null
    private lateinit var viewModel: ExamResultsViewModel
    private val examResultListAdapter = ExamResultsAdapter(arrayListOf())

    private var studentUid: String? = null
    private var studentClassName: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        examResults = ArrayList()

        prefHelper = SharedPreferencesHelper(activity!!.applicationContext)

        arguments?.let {
            studentUid = ExamResultsFragmentArgs.fromBundle(it).studentUid
            studentClassName = ExamResultsFragmentArgs.fromBundle(it).className
            println("Student uid is $studentUid")
        }



        // (activity as HideShowIconInterface).showBackIcon()
        auth = FirebaseAuth.getInstance()
        //database = FirebaseDatabase.getInstance()
        //databaseReference = database!!.reference.child("Users").child("Students")

        viewModel = ViewModelProviders.of(this).get(ExamResultsViewModel::class.java)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_results, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // observeViewModel()
        val userType = prefHelper.getUserType()
        if(userType.equals("Students")){
            fetchExamResults(auth!!.currentUser?.uid!!, prefHelper.getClassName()!!)
        }
        else if(userType.equals("Volunteers") ){
            studentUid?.let {uid->
                studentClassName?.let {className->
                    fetchExamResults(uid, className)
                }
            }
        }

    }

    fun fetchExamResults(uid: String, className: String) {

        val refNew = FirebaseFirestore.getInstance()

        refNew.collection("Users/Students/$className/User-$uid/exam results")
            .get().addOnSuccessListener { documents->
                val adapter = GroupAdapter<ViewHolder>()

                for(document in documents){
                    val result = document.toObject(ExamResult::class.java)
                    result?.let {
                        adapter.add(ExamResultItem(result))

                        val turkceNet = result.turkceD - result.turkceY*0.25
                        val matNet = result.matD - result.matY*0.25
                        val fenNet = result.fenD - result.fenY*0.25
                        val sosyalNet = result.sosyalD - result.sosyalY*0.25

                        result.totalPoint = 100 + (turkceNet + matNet)*3.3 + (fenNet + sosyalNet)*3.4

                        viewModel.addResultToTheDatabase(result)
                    }
                }
                recyclerExamList?.adapter = adapter
                recyclerExamList?.scrollToPosition(adapter.itemCount-1)
            }


        // OLD FIREBASE REALTIME DATABASE CODES
       /* val ref = FirebaseDatabase.getInstance().getReference("/Users/Students/$className/User-$currentUserUid/exam results")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val result = it.getValue(ExamResult::class.java)
                    result?.let {
                        adapter.add(ExamResultItem(result))

                        val turkceNet = result.turkceD - result.turkceY*0.25
                        val matNet = result.matD - result.matY*0.25
                        val fenNet = result.fenD - result.fenY*0.25
                        val sosyalNet = result.sosyalD - result.sosyalY*0.25

                        result.totalPoint = 100 + (turkceNet + matNet)*3.3 + (fenNet + sosyalNet)*3.4

                        viewModel.addResultToTheDatabase(result)
                    }
                }

                recyclerExamList?.adapter = adapter
                recyclerExamList?.scrollToPosition(adapter.itemCount-1)
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        */
    }


    class ExamResultItem(val result: ExamResult): Item<ViewHolder>() {
        override fun getLayout(): Int {
            return R.layout.row_exam_result
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            // will be called for each object in the list
            val item = viewHolder.itemView

            item.name.text = result.examName

            item.resultMatD.text = result.matD.toString() + " D "
            item.resultMatY.text = result.matY.toString() + " Y "
            var matB =result.matT - (result.matD + result.matY)
            if(matB<0) matB = 0
            item.resultMatB.text = matB.toString() + " B "

            item.resultTurD.text = result.turkceD.toString() + " D "
            item.resultTurY.text = result.turkceY.toString() + " Y "
            var turB =result.turkceT - (result.turkceD + result.turkceY)
            if(turB<0) turB = 0
            item.resultTurB.text = turB.toString() + " B "

            item.resultFenD.text = result.fenD.toString() + " D "
            item.resultFenY.text = result.fenY.toString() + " Y "
            var fenB =result.fenT - (result.fenD + result.fenY)
            if(fenB<0) fenB = 0
            item.resultFenB.text = fenB.toString() + " B "

            item.resultSosyalD.text = result.sosyalD.toString() + " D "
            item.resultSosyalY.text = result.sosyalY.toString() + " Y "
            var sosyalB =result.sosyalT - (result.sosyalD + result.sosyalY)
            if(sosyalB<0) sosyalB = 0
            item.resultSosyalB.text = sosyalB.toString() + " B "

        }

    }






    fun observeViewModel() {
        viewModel.examResultsLiveData.observe(this, Observer {results ->
            results?.let {
                recyclerExamList.visibility = View.VISIBLE
                examResultListAdapter.updateExamResultList(results)
                println("OBSERVE CALLED")
               // println("Mat: "+results[0].matD)
            }
        })

        viewModel.examResultsLoadError.observe(this, Observer {isError ->
            isError?.let {
                errorExamResult.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.examResultsLoading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingExamResults.visibility = if(it) View.VISIBLE else View.INVISIBLE
                if(it) {
                    errorExamResult.visibility = View.GONE
                    recyclerExamList.visibility = View.GONE
                }
            }
        })


    }



}
