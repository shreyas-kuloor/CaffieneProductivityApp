package com.example.caffieneproductivityapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logBtn : Button = findViewById(R.id.log_a_drink_btn)
        val dataBtn : Button = findViewById(R.id.view_data_btn)

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

        logBtn.setOnClickListener {
            val logIntent = Intent(this,LogDrinkActivity::class.java)
            startActivity(logIntent)
        }

        dataBtn.setOnClickListener {
            val histIntent = Intent(this,ViewDataActivity::class.java)
            startActivity(histIntent)
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
                val histIntent = Intent(this, ViewHistoryActivity::class.java)
                startActivity(histIntent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
