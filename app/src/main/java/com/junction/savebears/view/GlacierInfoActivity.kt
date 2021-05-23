package com.junction.savebears.view

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.junction.savebears.R
import com.junction.savebears.adapter.GlacierInfoAdapter
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityGlacierInfoBinding
import com.junction.savebears.model.Glacier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class GlacierInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityGlacierInfoBinding
    private val adapter = GlacierInfoAdapter()
    private var isRight = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlacierInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewAdapter()
        showGraph()
        binding.btnBottomMenu.setOnClickListener {
            isRight = !isRight
            when (isRight) {
                true -> {
                    binding.mpChart.isVisible = true
                    binding.rvGlacierInfo.isVisible = false
                    binding.btnBottomMenu.text = getString(R.string.see_graph)
                }
                false -> {
                    binding.mpChart.isVisible = false
                    binding.rvGlacierInfo.isVisible = true
                    binding.btnBottomMenu.text = getString(R.string.go_glacier_pic)
                }
            }
        }
    }

    private fun setRecyclerViewAdapter() {
        binding.rvGlacierInfo.apply {
            adapter = this@GlacierInfoActivity.adapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        showAllGlacierInfos()
    }

    private fun showAllGlacierInfos() {
        val dummyUrl = "https://c402277.ssl.cf1.rackcdn.com/photos/3218/images/blog_show/Alaska_June_2010_053.jpg?1357107480"
        val dummy = Glacier(dummyUrl, "2020-02-21")
        val list = mutableListOf<Glacier>()
        (0..9).forEach { list.add(dummy) }
        adapter.addItem(list)
    }

    private fun showGraph() {
        with(binding) {
            mpChart.description.isEnabled = false
            mpChart.setBackgroundColor(Color.WHITE)
            mpChart.setDrawGridBackground(false)
            mpChart.setDrawBarShadow(false)
            mpChart.isHighlightFullBarEnabled = false

            // draw bars behind lines
            mpChart.drawOrder = arrayOf(
                CombinedChart.DrawOrder.LINE
            )

            val l: Legend = mpChart.legend
            l.isWordWrapEnabled = true
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)

            val rightAxis: YAxis = mpChart.axisRight
            rightAxis.setDrawGridLines(false)
            rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val leftAxis: YAxis = mpChart.axisLeft
            leftAxis.setDrawGridLines(false)
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val xAxis: XAxis = mpChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f

            val data = CombinedData()

            data.setData(generateLineData())

            xAxis.axisMaximum = data.xMax + 0.25f

            mpChart.data = data
            mpChart.invalidate()
        }
    }

    private fun generateLineData(): LineData {
        val list = (0..9).toList()
        val d = LineData()
        val entries = ArrayList<Entry>()
        for (index in 0 until list.count()) {
            entries.add(Entry(index + 0.5f, getRandom(15f, 5f)))
        }
        val set = LineDataSet(entries, "Glacier Melting")
        set.color = Color.RED
        set.lineWidth = 2.5f
        set.setCircleColor(Color.BLUE)
        set.circleRadius = 5f
        set.fillColor = Color.RED
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 20f
        set.valueTextColor = Color.BLACK
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    private fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }

}