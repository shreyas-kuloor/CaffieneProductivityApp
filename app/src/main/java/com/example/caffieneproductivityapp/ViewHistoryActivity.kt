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
import com.example.caffieneproductivityapp.persistence.DrinkRatingViewModel
import com.example.caffieneproductivityapp.persistence.Rating
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.google.android.material.navigation.NavigationView
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField


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
                val chart : LineChart = findViewById(R.id.chart)
                val data : ArrayList<Entry> = ArrayList()
                ratingsList.forEach {
                    if (it.timeStamp in getTodayStart()..getTodayEnd()) {
                        data.add(Entry(it.timeStamp.toFloat(), it.productivityRating.toFloat()))
                    }
                }
                val dataSet = LineDataSet(data, "Productivity Ratings")
                dataSet.color = getColor(R.color.colorPrimary)
                dataSet.valueTextColor = getColor(R.color.colorPrimaryDark)

                val format = DateTimeFormatter.ofPattern("HH:mm")
                val xAxis = chart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                val formatter = IAxisValueFormatter { value, _ ->
                    val date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.of(ZoneId.systemDefault().toString()))
                    date.format(format)
                }
                xAxis.granularity = 1f
                xAxis.textSize = 1f

                xAxis.axisMinimum = getTodayStart().toFloat()
                xAxis.axisMaximum = getTodayEnd().toFloat()

                xAxis.valueFormatter = formatter

                val yAxisLeft : YAxis = chart.axisLeft
                yAxisLeft.granularity = 1f
                yAxisLeft.axisMinimum = 0f
                yAxisLeft.axisMaximum = 5f

                val yAxisRight: YAxis = chart.axisRight
                yAxisRight.isEnabled = false

                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.animateX(2500)
                chart.description.text = "Daily Productivity Over Time"
                chart.invalidate()

            }
            R.id.weekly_filter -> {
                item.isChecked = true
                val chart : LineChart = findViewById(R.id.chart)
                val data : ArrayList<Entry> = ArrayList()
                ratingsList.forEach {
                    if (it.timeStamp in getWeekStart()..getWeekEnd()) {
                        data.add(Entry(it.timeStamp.toFloat(), it.productivityRating.toFloat()))
                    }
                }
                val dataSet = LineDataSet(data, "Productivity Ratings")
                dataSet.color = getColor(R.color.colorPrimary)
                dataSet.valueTextColor = getColor(R.color.colorPrimaryDark)

                val format = DateTimeFormatter.ofPattern("MMM dd HH:mm")
                val xAxis = chart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                val formatter = IAxisValueFormatter { value, _ ->
                    val date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.of(ZoneId.systemDefault().toString()))
                    date.format(format)
                }
                xAxis.granularity = 1f
                xAxis.textSize = 1f

                xAxis.setLabelCount(8 , true)
                xAxis.axisMinimum = getWeekStart().toFloat()
                xAxis.axisMaximum = getWeekEnd().toFloat()

                xAxis.valueFormatter = formatter

                val yAxisLeft : YAxis = chart.axisLeft
                yAxisLeft.granularity = 1f
                yAxisLeft.axisMinimum = 0f
                yAxisLeft.axisMaximum = 5f

                val yAxisRight: YAxis = chart.axisRight
                yAxisRight.isEnabled = false

                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.animateX(2500)
                chart.description.text = "Weekly Productivity Over Time"
                chart.invalidate()
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

    private fun getTodayStart() : Long {
        val timeZone : String = ZoneId.systemDefault().toString()
        val now = ZonedDateTime.now(ZoneId.of(timeZone))
        val todayStart : ZonedDateTime = now.toLocalDate().atStartOfDay(ZoneId.of(timeZone))
        return todayStart.toInstant().toEpochMilli()
    }

    private fun getTodayEnd() : Long {
        val timeZone : String = ZoneId.systemDefault().toString()
        val now = ZonedDateTime.now(ZoneId.of(timeZone))
        val todayEnd : ZonedDateTime = now.toLocalDate().plusDays(1).atStartOfDay(ZoneId.of(timeZone))
        return todayEnd.toInstant().toEpochMilli()
    }

    private fun getWeekStart() : Long {
        val timeZone : String = ZoneId.systemDefault().toString()
        val now = ZonedDateTime.now(ZoneId.of(ZoneId.systemDefault().toString()))
        val weekStart : ZonedDateTime = now.with(ChronoField.DAY_OF_WEEK, 1).toLocalDate().atStartOfDay(ZoneId.of(timeZone))
        return weekStart.toInstant().toEpochMilli()
    }
    private fun getWeekEnd() : Long {
        val timeZone : String = ZoneId.systemDefault().toString()
        val now = ZonedDateTime.now(ZoneId.of(ZoneId.systemDefault().toString()))
        val weekEnd : ZonedDateTime = now.with(ChronoField.DAY_OF_WEEK, 1).toLocalDate().plusWeeks(1).atStartOfDay(ZoneId.of(timeZone))
        return weekEnd.toInstant().toEpochMilli()
    }

    private enum class ChartType {
        Daily,
        Weekly,
        Monthly,
        Yearly
    }

}


