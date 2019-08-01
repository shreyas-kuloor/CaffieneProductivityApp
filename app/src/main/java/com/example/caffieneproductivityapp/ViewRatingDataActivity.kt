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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caffieneproductivityapp.persistence.DrinkRatingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ViewRatingDataActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drinkRatingViewModel: DrinkRatingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rating_data)
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

        val recyclerView = findViewById<RecyclerView>(R.id.view_rating_data_recycler_view)
        val adapter = RatingAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab : FloatingActionButton = findViewById(R.id.fab)

        val drinkId : Int = intent.getIntExtra("DrinkID", 1000)
        val latest : Boolean = intent.getBooleanExtra("Latest", false)

        supportActionBar?.title = getString(R.string.rating_action_bar_title, drinkId)

        drinkRatingViewModel = ViewModelProviders.of(this).get(DrinkRatingViewModel::class.java)
        drinkRatingViewModel.getRatingsForDrink(drinkId).observe(this, Observer { ratings ->
            ratings?.let { adapter.setRatings(it) }
        })

        if (!latest) {
            fab.hide()
        }
        fab.setOnClickListener {
            val ratingIntent = Intent(this, RateProductivityActivity::class.java)
            startActivity(ratingIntent)
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val dataIntent = Intent(this, ViewDataActivity::class.java)
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
                val dataIntent = Intent(this, ViewDataActivity::class.java)
                startActivity(dataIntent)
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
