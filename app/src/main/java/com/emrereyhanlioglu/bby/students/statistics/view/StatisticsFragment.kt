package com.emrereyhanlioglu.bby.students.statistics.view


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResult
import com.emrereyhanlioglu.bby.students.examResults.model.ExamResultDatabase
import com.emrereyhanlioglu.bby.students.statistics.model.StatisticsViewModel
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.coroutines.launch




class StatisticsFragment : Fragment() {
    private lateinit var viewModel: StatisticsViewModel
    private var examResults: List<ExamResult>? = null

    private var dataPointsTotal: ArrayList<DataPoint>? = null
    private var dataPointsMath: ArrayList<DataPoint>? = null
    private var dataPointsTurkish: ArrayList<DataPoint>? = null
    private var dataPointsScience: ArrayList<DataPoint>? = null
    private var dataPointsSocial: ArrayList<DataPoint>? = null


    private var examsTotalPoints: ArrayList<Double>? = null
    private var mathResults: ArrayList<Double>? = null
    private var turkishResults: ArrayList<Double>? = null
    private var scienceResults: ArrayList<Double>? = null
    private var socialResults: ArrayList<Double>? = null

  //  private var dataPointsTotal: Array<DataPoint>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        dataPointsTotal = arrayListOf()
        dataPointsMath = arrayListOf()
        dataPointsTurkish = arrayListOf()
        dataPointsScience = arrayListOf()
        dataPointsSocial = arrayListOf()


        examsTotalPoints = arrayListOf()
        mathResults = arrayListOf()
        turkishResults = arrayListOf()
        scienceResults = arrayListOf()
        socialResults = arrayListOf()


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.emrereyhanlioglu.bby.R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            whenStarted {
                // The block inside will run only when Lifecycle is at least STARTED.
                // It will start executing when fragment is started and
                // can call other suspend methods.

                examResults = ExamResultDatabase(activity!!.applicationContext).examResultDao().getAllResults()

            }
            // This line runs only after the whenStarted block above has completed.

            setupTotalPointGraph()
            setupMathGraph()
            setupTurkishGraph()
            setupScienceGraph()
            setupSocialGraph()


        }


    }

    private fun setGraphTextSize(numberOfValues: Int, graph: GraphView) {
        if(numberOfValues < 10) {
            graph.gridLabelRenderer.textSize = 30F
            return
        }
        else if(numberOfValues < 20) {
            graph.gridLabelRenderer.textSize = 24F
            return
        }
        else {
            graph.gridLabelRenderer.textSize = 18F
            return
        }
    }


    private fun setupTotalPointGraph() {
        if(examResults!= null){
            var i=0
            while(i < examResults!!.size) {
                examResults?.let {
                    examsTotalPoints?.add(it[i].totalPoint)
                }
                i++
            }

            var j=0
            while(j < examsTotalPoints!!.size) {

                dataPointsTotal.let { dp ->
                    examsTotalPoints?.let {
                        dp!!.add( DataPoint( (j+1.0), it[j]) )
                    }
                }
                j++
            }

            val array = arrayOfNulls<DataPoint>(examsTotalPoints!!.size)
            val series = BarGraphSeries(dataPointsTotal!!.toArray(array))

            graph.addSeries(series)
           // graph.title = "Deneme Puanlarım"


            val gridLabel = graph.getGridLabelRenderer()
            gridLabel.horizontalAxisTitle = "Denemeler"
            gridLabel.verticalAxisTitle = "Toplam Puan"
            setGraphTextSize(dataPointsTotal!!.size, graph)



            series.setValueDependentColor { data ->
                Color.rgb(
                    data!!.x.toInt() * 255 / 4,
                    Math.abs(data.y * 255 / 6).toInt(),
                    100
                )
            }

            if(examsTotalPoints != null) {
                graph.viewport.setMaxX(examsTotalPoints!!.size+2.0)
                graph.viewport.setMinX(0.0)
                graph.viewport.setMaxY(550.0)
                graph.viewport.setMinY(0.0)

                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.isYAxisBoundsManual = true
            }

            series.spacing = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED


        }
    }



    private fun setupMathGraph() {
        if(examResults!= null){
            var i=0
            while(i < examResults!!.size) {
                examResults?.let {
                    mathResults?.add(it[i].matD - it[i].matY*0.25)
                }
                i++
            }

            var j=0
            while(j < mathResults!!.size) {

                dataPointsMath.let { dp ->
                    mathResults?.let {
                        dp!!.add( DataPoint( (j+1.0), it[j]) )
                    }
                }
                j++
            }

            val array = arrayOfNulls<DataPoint>(mathResults!!.size)
            val series = BarGraphSeries(dataPointsMath!!.toArray(array))

            graphMath.addSeries(series)
            // graph.title = ""


            val gridLabel = graphMath.getGridLabelRenderer()
            gridLabel.horizontalAxisTitle = "Denemeler"
            gridLabel.verticalAxisTitle = "Net"
            setGraphTextSize(mathResults!!.size, graphMath)



            series.setValueDependentColor { data ->
                Color.rgb(
                    data!!.x.toInt() * 255 / 4,
                    Math.abs(data.y * 255 / 6).toInt(),
                    100
                )
            }

            if(mathResults != null) {
                graphMath.viewport.setMaxX(mathResults!!.size+2.0)
                graphMath.viewport.setMinX(0.0)
                graphMath.viewport.setMaxY(45.0)
                graphMath.viewport.setMinY(-10.0)

                graphMath.viewport.isXAxisBoundsManual = true
                graphMath.viewport.isYAxisBoundsManual = true
            }

            series.spacing = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED


        }
    }



    private fun setupTurkishGraph() {
        if(examResults!= null){
            var i=0
            while(i < examResults!!.size) {
                examResults?.let {
                    turkishResults?.add(it[i].turkceD - it[i].turkceY*0.25)
                }
                i++
            }

            var j=0
            while(j < turkishResults!!.size) {

                dataPointsTurkish.let { dp ->
                    turkishResults?.let {
                        dp!!.add( DataPoint( (j+1.0), it[j]) )
                    }
                }
                j++
            }

            val array = arrayOfNulls<DataPoint>(turkishResults!!.size)
            val series = BarGraphSeries(dataPointsTurkish!!.toArray(array))

            graphTurkish.addSeries(series)

            // graph.title = ""


            val gridLabel = graphTurkish.getGridLabelRenderer()
            gridLabel.horizontalAxisTitle = "Denemeler"
            gridLabel.verticalAxisTitle = "Net"
            setGraphTextSize(turkishResults!!.size, graphTurkish)


            series.setValueDependentColor { data ->
                Color.rgb(
                    data!!.x.toInt() * 255 / 4,
                    Math.abs(data.y * 255 / 6).toInt(),
                    100
                )
            }

            if(turkishResults != null) {
                graphTurkish.viewport.setMaxX(turkishResults!!.size+2.0)
                graphTurkish.viewport.setMinX(0.0)
                graphTurkish.viewport.setMaxY(45.0)
                graphTurkish.viewport.setMinY(-10.0)

                graphTurkish.viewport.isXAxisBoundsManual = true
                graphTurkish.viewport.isYAxisBoundsManual = true
            }

            series.spacing = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED

        }
    }



    private fun setupScienceGraph() {
        if(examResults!= null){
            var i=0
            while(i < examResults!!.size) {
                examResults?.let {
                    scienceResults?.add(it[i].fenD - it[i].fenY*0.25)
                }
                i++
            }

            var j=0
            while(j < scienceResults!!.size) {

                dataPointsScience.let { dp ->
                    scienceResults?.let {
                        dp!!.add( DataPoint( (j+1.0), it[j]) )
                    }
                }
                j++
            }

            val array = arrayOfNulls<DataPoint>(scienceResults!!.size)
            val series = BarGraphSeries(dataPointsScience!!.toArray(array))

            graphScience.addSeries(series)

            // graph.title = ""


            val gridLabel = graphScience.getGridLabelRenderer()
            gridLabel.horizontalAxisTitle = "Denemeler"
            gridLabel.verticalAxisTitle = "Net"
            setGraphTextSize(scienceResults!!.size, graphScience)


            series.setValueDependentColor { data ->
                Color.rgb(
                    data!!.x.toInt() * 255 / 4,
                    Math.abs(data.y * 255 / 6).toInt(),
                    100
                )
            }

            if(scienceResults != null) {
                graphScience.viewport.setMaxX(scienceResults!!.size+2.0)
                graphScience.viewport.setMinX(0.0)
                graphScience.viewport.setMaxY(25.0)
                graphScience.viewport.setMinY(-5.0)

                graphScience.viewport.isXAxisBoundsManual = true
                graphScience.viewport.isYAxisBoundsManual = true
            }

            series.spacing = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED

        }
    }



    private fun setupSocialGraph() {
        if(examResults!= null){
            var i=0
            while(i < examResults!!.size) {
                examResults?.let {
                    socialResults?.add(it[i].sosyalD - it[i].sosyalY*0.25)
                }
                i++
            }

            var j=0
            while(j < scienceResults!!.size) {

                dataPointsSocial.let { dp ->
                    socialResults?.let {
                        dp!!.add( DataPoint( (j+1.0), it[j]) )
                    }
                }
                j++
            }

            val array = arrayOfNulls<DataPoint>(socialResults!!.size)
            val series = BarGraphSeries(dataPointsSocial!!.toArray(array))

            graphSocial.addSeries(series)

            // graph.title = ""


            val gridLabel = graphSocial.getGridLabelRenderer()
            gridLabel.horizontalAxisTitle = "Denemeler"
            gridLabel.verticalAxisTitle = "Net"
            setGraphTextSize(socialResults!!.size, graphSocial)


            series.setValueDependentColor { data ->
                Color.rgb(
                    data!!.x.toInt() * 255 / 4,
                    Math.abs(data.y * 255 / 6).toInt(),
                    100
                )
            }

            if(socialResults != null) {
                graphSocial.viewport.setMaxX(socialResults!!.size+2.0)
                graphSocial.viewport.setMinX(0.0)
                graphSocial.viewport.setMaxY(25.0)
                graphSocial.viewport.setMinY(-5.0)

                graphSocial.viewport.isXAxisBoundsManual = true
                graphSocial.viewport.isYAxisBoundsManual = true
            }

            series.spacing = 50
            series.isDrawValuesOnTop = true
            series.valuesOnTopColor = Color.RED

        }
    }



    fun observeViewModel() {
        viewModel.examResults.observe(this, Observer { results ->
            results?.let {

               // examResultListAdapter.updateExamResultList(results)
                println("OBSERVE CALLED")
                examResults = it as ArrayList<ExamResult>

            }
        })
    }


}
