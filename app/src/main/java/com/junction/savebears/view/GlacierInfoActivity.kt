package com.junction.savebears.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.junction.savebears.R
import com.junction.savebears.adapter.GlacierInfoAdapter
import com.junction.savebears.base.BaseActivity
import com.junction.savebears.databinding.ActivityGlacierInfoBinding
import com.junction.savebears.remote.model.Glacier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class GlacierInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityGlacierInfoBinding
    private val adapter = GlacierInfoAdapter()
    private var isRight = false
    private val glacierInfos = mutableListOf<Glacier>()
    private var totalChanges = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlacierInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecyclerViewAdapter()
        binding.btnBottomMenu.setOnClickListener {
            isRight = !isRight
            when (isRight) {
                true -> {
                    binding.mpChart.isVisible = isRight
                    binding.rvGlacierInfo.isVisible = !isRight
                    binding.btnBottomMenu.text = getString(R.string.see_glacier_pics)
                }
                false -> {
                    binding.mpChart.isVisible = isRight
                    binding.rvGlacierInfo.isVisible = !isRight
                    binding.btnBottomMenu.text = getString(R.string.see_graph)
                }
            }
        }
    }

    private fun setRecyclerViewAdapter() {
        binding.rvGlacierInfo.apply {
            adapter = this@GlacierInfoActivity.adapter

        }
        showAllGlacierInfos()
    }

    private fun showAllGlacierInfos() {
        val data = flow { emit(saveBearsApi.getGlacierChangeData()) }
        lifecycleScope.launch {
            data.catch { Timber.i(it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    it.data1984.date = COLUMN_1
                    it.data1994.date = COLUMN_2
                    it.data2004.date = COLUMN_3
                    it.data2014.date = COLUMN_4

                    glacierInfos.add(it.data1984)
                    glacierInfos.add(it.data1994)
                    glacierInfos.add(it.data2004)
                    glacierInfos.add(it.data2014)

                    totalChanges += (it.data1984.changeAmount + it.data1994.changeAmount + it.data2004.changeAmount + it.data2014.changeAmount)
                    binding.tvCommentTop.text = "현재 지구의 빙하는 1980년부터\n7%씩 감소해서 ${totalChanges}%입니다."

                    adapter.addItem(glacierInfos)

                    showGraph()
                }
        }
    }

    fun setBoldText(s: CharSequence, target: String): SpannableStringBuilder {
        val str = SpannableStringBuilder(s)
        val p = Pattern.compile(target, Pattern.CASE_INSENSITIVE)
        val m = p.matcher(s)
        while (m.find()) {
            str.setSpan(StyleSpan(Typeface.BOLD), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
        return str
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
        val d = LineData()
        val entries = ArrayList<Entry>()
        entries.add(Entry(0.5f, glacierInfos[0].changeAmount))
        entries.add(Entry(1.5f, glacierInfos[1].changeAmount))
        entries.add(Entry(2.5f, glacierInfos[2].changeAmount))
        entries.add(Entry(3.5f, glacierInfos[3].changeAmount))

        val xAxisLabels: ArrayList<String> = ArrayList()
        xAxisLabels.add(COLUMN_1)
        xAxisLabels.add(COLUMN_2)
        xAxisLabels.add(COLUMN_3)
        xAxisLabels.add(COLUMN_4)

        binding.mpChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

        val set = LineDataSet(entries, GRAPH_TITLE)
        set.color = ContextCompat.getColor(this, R.color.blue_200)
        set.lineWidth = resources.getDimension(R.dimen.length10)
        set.setCircleColor(ContextCompat.getColor(this, R.color.blue))
        set.circleRadius = resources.getDimension(R.dimen.corner_radius4)
        set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = resources.getDimension(R.dimen.text_size10)
        set.valueTextColor = Color.BLACK
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    companion object {
        private const val COLUMN_1 = "1984"
        private const val COLUMN_2 = "1994"
        private const val COLUMN_3 = "2004"
        private const val COLUMN_4 = "2014"
        private const val GRAPH_TITLE = "Glacier Melting"
    }

}