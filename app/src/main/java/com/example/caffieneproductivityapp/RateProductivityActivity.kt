package com.example.caffieneproductivityapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caffieneproductivityapp.persistence.Drink
import com.example.caffieneproductivityapp.persistence.DrinkRatingViewModel
import com.example.caffieneproductivityapp.persistence.Rating
import com.google.android.material.navigation.NavigationView

class RateProductivityActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drinkRatingViewModel : DrinkRatingViewModel
    private var lastDrink : Drink? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_productivity)

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

        supportActionBar?.title = getString(R.string.title_activity_rate_productivity)

        val seekBar : SeekBar = findViewById(R.id.rate_productivity_slider)
        val ratingTextView : TextView = findViewById(R.id.rating_num_text)
        val startingProgress = 2
        var value : Int = startingProgress + 1
        seekBar.progress = startingProgress
        ratingTextView.text = value.toString()

        val submitBtn : Button = findViewById(R.id.submit_button)

        drinkRatingViewModel = ViewModelProviders.of(this).get(DrinkRatingViewModel::class.java)
        drinkRatingViewModel.getLastDrink().observe(this, Observer { drink ->
            lastDrink = drink
        })


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                value = progress + 1
                ratingTextView.text = value.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        submitBtn.setOnClickListener {
            val rating = Rating(
                0,
                System.currentTimeMillis(),
                value,
                lastDrink?.id
            )
            drinkRatingViewModel.insertRating(rating)

            val dataIntent = Intent(this, ViewRatingDataActivity::class.java)
            dataIntent.putExtra("DrinkID", lastDrink?.id)
            dataIntent.putExtra("Latest", true)
            startActivity(dataIntent)
        }
        
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.thank_you, menu)
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
                val logIntent = Intent(this, LogDrinkActivity::class.java)
                startActivity(logIntent)
            }
            R.id.nav_data -> {
                val testIntent = Intent(this, ViewDataActivity::class.java)
                startActivity(testIntent)
            }
            R.id.nav_rate -> {
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
