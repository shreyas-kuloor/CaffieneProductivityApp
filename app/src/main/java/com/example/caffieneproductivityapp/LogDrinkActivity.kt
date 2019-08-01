package com.example.caffieneproductivityapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.example.caffieneproductivityapp.persistence.Drink
import com.example.caffieneproductivityapp.persistence.DrinkRatingViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_log_drink.*


class LogDrinkActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drinkRatingViewModel: DrinkRatingViewModel
    val REMINDER_ENABLED = "notification"
    val REMINDER_FREQUENCY = "frequency"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_log_drink)

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

        supportActionBar?.title = getString(R.string.log_drink)

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView : View = inflater.inflate(R.layout.popup_window, null)
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, height, width, false)
        val popupText = popupView.findViewById(R.id.popup_text) as TextView

        val mainIntent = Intent(this, MainActivity::class.java)
        val popupBtn = popupView.findViewById(R.id.popup_ok_button) as Button
        popupBtn.setOnClickListener {
            popupWindow.dismiss()
            startActivity(mainIntent)
        }

        drinkRatingViewModel = ViewModelProviders.of(this).get(DrinkRatingViewModel::class.java)


        val intentAlarm = Intent(this, NotificationService::class.java)
        val alarmMgr : AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0)

        small_coffee_btn.setOnClickListener {
            alarmMgr.cancel(pendingIntent)
            popupText.text = getText(R.string.thank_you_msg_small)
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            createDrink(DrinkSize.Small, drinkRatingViewModel, alarmMgr, pendingIntent)
        }
        med_coffee_btn.setOnClickListener {
            alarmMgr.cancel(pendingIntent)
            popupText.text = getText(R.string.thank_you_msg_med)
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            createDrink(DrinkSize.Medium, drinkRatingViewModel, alarmMgr, pendingIntent)
        }
        lrg_coffee_btn.setOnClickListener {
            alarmMgr.cancel(pendingIntent)
            popupText.text = getText(R.string.thank_you_msg_lrg)
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            createDrink(DrinkSize.Large, drinkRatingViewModel, alarmMgr, pendingIntent)
        }

    }

    private fun createDrink(drinkSize : DrinkSize, drinkRatingViewModel: DrinkRatingViewModel, alarmManager: AlarmManager, pendingIntent: PendingIntent) {
        val drink = Drink(
            0,
            drinkSize,
            System.currentTimeMillis()
        )
        drinkRatingViewModel.insertDrink(drink)
        val sp : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val reminderFreq : Long = sp.getString(REMINDER_FREQUENCY, "900000").toLong()
        val reminderEnabled : Boolean = sp.getBoolean(REMINDER_ENABLED, true)

        if (reminderEnabled) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), reminderFreq, pendingIntent)
        }

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == REMINDER_ENABLED && !prefs.getBoolean(REMINDER_ENABLED, true)) {
                alarmManager.cancel(pendingIntent)
            }
        }

        sp.registerOnSharedPreferenceChangeListener(listener)

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
        menuInflater.inflate(R.menu.log_drink, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar drink_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view drink_item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
            }
            R.id.nav_drink -> {

            }
            R.id.nav_data -> {
                val testIntent = Intent(this, ViewDataActivity::class.java)
                startActivity(testIntent)
            }
            R.id.nav_rate -> {
                val rateIntent = Intent(this, RateProductivityActivity::class.java)
                startActivity(rateIntent)
            }
            R.id.nav_history -> {
                val histIntent = Intent(this, ViewHistoryActivity::class.java)
                startActivity(histIntent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
