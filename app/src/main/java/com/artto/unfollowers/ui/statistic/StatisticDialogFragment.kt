package com.artto.unfollowers.ui.statistic

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.unfollowers.R
import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import com.artto.unfollowers.ui.base.BaseDialogFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import kotlinx.android.synthetic.main.dialog_statistic.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class StatisticDialogFragment : BaseDialogFragment(), StatisticView {

    @InjectPresenter
    lateinit var presenter: StatisticPresenter

    @ProvidePresenter
    fun providePresenter() = inject<StatisticPresenter>().value

    private val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())

    private var chartAxisTextColor = Color.GRAY
    private var chartGridColor = Color.GRAY

    private var colorFollowers = Color.BLUE
    private var colorFollowing = Color.GREEN
    private var colorUnfollowers = Color.RED

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            chartGridColor = ContextCompat.getColor(it, R.color.gray)

            colorFollowers = ContextCompat.getColor(it, R.color.followers)
            colorFollowing = ContextCompat.getColor(it, R.color.following)
            colorUnfollowers = ContextCompat.getColor(it, R.color.unfollowers)
        }

        setupLineChartView()
        setupPieChartView()
    }

    private fun setupLineChartView() {
        lc_statistic_line.apply {
            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false
            description.text = ""
            setTouchEnabled(true)
            setScaleEnabled(false)
            axisRight.isEnabled = false

            with(axisLeft) {
                textColor = chartAxisTextColor
                gridColor = chartGridColor
                textSize = 11f
            }

            with(xAxis) {
                textColor = chartAxisTextColor
                gridColor = chartGridColor
                position = XAxis.XAxisPosition.BOTTOM
                textSize = 11f
                granularity = 1f

            }
        }
    }

    private fun setupPieChartView() {
        pc_statistic_pie.apply {
            legend.isEnabled = false
            description.text = ""
        }
    }

    override fun showData(data: List<StatisticEntity>) {
        //Line chart
        val followersDataSet = LineDataSet(data.mapIndexed { i, entity -> Entry(i.toFloat(), entity.followersCount.toFloat()) }, "")
        val followingDataSet = LineDataSet(data.mapIndexed { i, entity -> Entry(i.toFloat(), entity.followingCount.toFloat()) }, "")
        val unfollowersDataSet = LineDataSet(data.mapIndexed { i, entity -> Entry(i.toFloat(), entity.unfollowersCount.toFloat()) }, "")
        setupDataSet(followersDataSet, colorFollowers)
        setupDataSet(followingDataSet, colorFollowing)
        setupDataSet(unfollowersDataSet, colorUnfollowers)

        val dateLabels = data.map { dateFormat.format(it.date) }

        with(lc_statistic_line) {
            xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
                value.toInt()
                        .takeIf { it in 0 until dateLabels.size }
                        ?.let { dateLabels[value.toInt()] }
                        ?: ""
            }

            this.data = LineData(listOf(followersDataSet, followingDataSet, unfollowersDataSet))
            setVisibleXRangeMaximum(5f)
            moveViewToX(this.data.xMax)
        }

        //Pie chart
        val lastRecord = data.last()
        val pieEntries = lastRecord.let {
            arrayListOf(
                    PieEntry(it.followingCount.toFloat(), ""),
                    PieEntry(it.unfollowersCount.toFloat(), ""),
                    PieEntry(it.followersCount.toFloat(), ""))
        }
        val pieDataSet = PieDataSet(pieEntries, "").apply {
            valueFormatter = IValueFormatter { _, _, _, _ -> "" }
            colors = arrayListOf(colorFollowing, colorUnfollowers, colorFollowers)
        }
        pc_statistic_pie.data = PieData(pieDataSet)
        pc_statistic_pie.invalidate()

        //Text views
        tv_statistic_followers.text = getString(R.string.statistic_followers, lastRecord.followersCount)
        tv_statistic_following.text = getString(R.string.statistic_following, lastRecord.followingCount)
        tv_statistic_unfollowers.text = getString(R.string.statistic_unfollowers, lastRecord.unfollowersCount)
    }

    private fun setupDataSet(dataSet: LineDataSet, lineColor: Int) {
        dataSet.apply {
            valueTextColor = Color.BLACK
            valueTextSize = 10f
            color = lineColor
            setCircleColor(Color.BLACK)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            isHighlightEnabled = false
            lineWidth = 2.5f
            circleHoleRadius = 1.75f
            circleRadius = 3.5f
        }
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            setLayout(
                    (resources.displayMetrics.widthPixels * 0.9).toInt(),
                    attributes.height)
        }
    }

    override fun getLayout() = R.layout.dialog_statistic

}