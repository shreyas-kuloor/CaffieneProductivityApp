package com.example.caffieneproductivityapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.scales.DateTime
import com.example.caffieneproductivityapp.persistence.DrinkRatingViewModel
import com.example.caffieneproductivityapp.persistence.Rating
import com.google.android.material.navigation.NavigationView
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class ViewHistoryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drinkRatingViewModel: DrinkRatingViewModel
    private var ratingsList : List<Rating> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        drinkRatingViewModel = ViewModelProviders.of(this).get(DrinkRatingViewModel::class.java)
        drinkRatingViewModel.allRatings.observe(this, Observer { ratings ->
            ratingsList = ratings
            println("SIZE =====" + ratingsList.size)
        })

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar drink_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            R.id.daily_filter -> {
                item.isChecked = true
                println("SIZE=========" + ratingsList.size)
                val chartView : AnyChartView = findViewById(R.id.any_chart_view)
                val chart : Cartesian = AnyChart.line()
                initChart(chart)
                setChartLimits(chart, ChartType.Daily)
                val timestamps : List<Long> = ratingsList.stream().map(Rating::timeStamp).collect(Collectors.toList())
                val productivities : List<Int> = ratingsList.stream().map(Rating::productivityRating).collect(Collectors.toList())

                val data : ArrayList<DataEntry> = ArrayList()
                val currentTime = System.currentTimeMillis()
                val midnightBefore = currentTime - currentTime % (24 * 60 * 60 * 1000)
                val midnightAfter = midnightBefore + 24 * 60 * 60 * 1000
                val maxIndex = timestamps.size - 1
                for (i in 0..maxIndex) {
                    val timestampsAdjusted = timestamps[i]-14400000
                    if (timestampsAdjusted in midnightBefore..midnightAfter) {
                        data.add(ValueDataEntry(timestampsAdjusted, productivities[i]))
                    }
                }
                val series : Line = chart.line(data)
                series.name("Productivity Rating")
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(MarkerType.CIRCLE).size(4)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)

                chartView.setChart(chart)
            }
            R.id.weekly_filter -> {
                item.isChecked = true
                println("SIZE=========" + ratingsList.size)
                val chartView : AnyChartView = findViewById(R.id.any_chart_view)
                val chart : Cartesian = AnyChart.line()
                initChart(chart)
                setChartLimits(chart, ChartType.Weekly)
                val timestamps : List<Long> = ratingsList.stream().map(Rating::timeStamp).collect(Collectors.toList())
                val productivities : List<Int> = ratingsList.stream().map(Rating::productivityRating).collect(Collectors.toList())


                val data : ArrayList<DataEntry> = ArrayList()
                var cal : Calendar = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.clear(Calendar.MINUTE)
                cal.clear(Calendar.SECOND)
                cal.clear(Calendar.MILLISECOND)
                cal.set(Calendar.DAY_OF_WEEK, 1)
                val firstDayOfWeek = cal.timeInMillis

                cal.add(Calendar.WEEK_OF_YEAR, 1)
                cal.add(Calendar.MILLISECOND, -1)
                val lastDayOfWeek = cal.timeInMillis
                val maxIndex = timestamps.size - 1
                for (i in 0..maxIndex) {
                    val timestampsAdjusted = timestamps[i]-14400000
                    if (timestampsAdjusted in firstDayOfWeek..lastDayOfWeek) {
                        data.add(ValueDataEntry(timestampsAdjusted, productivities[i]))
                    }
                }

                val series : Line = chart.line(data)
                series.name("Productivity Rating")
                series.hovered().markers().enabled(true)
                series.hovered().markers().type(MarkerType.CIRCLE).size(4)
                series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)

                chartView.setChart(chart)
            }
            R.id.monthly_filter -> {
                item.isChecked = true
            }
            R.id.yearly_filter -> {
                item.isChecked = true
            }
        }
        return false

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view drink_item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }
            R.id.nav_drink -> {
                val drinkIntent = Intent(this, LogDrinkActivity::class.java)
                startActivity(drinkIntent)
            }
            R.id.nav_data -> {
                val dataIntent = Intent(this, ViewDataActivity::class.java)
                startActivity(dataIntent)
            }
            R.id.nav_rate -> {
                val rateIntent = Intent(this, RateProductivityActivity::class.java)
                startActivity(rateIntent)
            }
            R.id.nav_history -> {
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initChart(chart: Cartesian) {
        chart.apply {
            title("Daily Productivity Over Time")
            yAxis(0).title("Rating")
            yScale().minimum(0)
            yScale().maximum(5)
            yScale().ticks().allowFractional(false)
            xAxis(0).title("Time")
        }
    }

    private fun setChartLimits(chart: Cartesian, type: ChartType) {
        when (type) {
            ChartType.Daily -> {
                val dateTimeScale = DateTime.instantiate()
                val currentTime = System.currentTimeMillis()
                val midnightBefore = currentTime - currentTime % (24 * 60 * 60 * 1000)
                val midnightAfter = midnightBefore + 24 * 60 * 60 * 1000
                dateTimeScale.ticks().interval(0,0,0,1,0, 0)
                dateTimeScale.minimum(midnightBefore)
                dateTimeScale.maximum(midnightAfter)
                chart.xScale(dateTimeScale)
            }
            ChartType.Weekly -> {
                val dateTimeScale = DateTime.instantiate()
                var cal : Calendar = Calendar.getInstance()
                cal.firstDayOfWeek
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.clear(Calendar.MINUTE)
                cal.clear(Calendar.SECOND)
                cal.clear(Calendar.MILLISECOND)
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                val firstDayOfWeek = cal.timeInMillis

                cal.add(Calendar.WEEK_OF_YEAR, 1)
                cal.add(Calendar.MILLISECOND, -1)
                val lastDayOfWeek = cal.timeInMillis

                dateTimeScale.ticks().interval(0,0,1,0,0, 0)
                dateTimeScale.minimum(firstDayOfWeek)
                dateTimeScale.maximum(lastDayOfWeek)
                chart.xScale(dateTimeScale)
            }
            ChartType.Monthly -> {

            }
            ChartType.Yearly -> {

            }
        }
    }

    private enum class ChartType {
        Daily,
        Weekly,
        Monthly,
        Yearly
    }

}


