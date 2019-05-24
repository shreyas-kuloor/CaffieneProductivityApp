package com.example.caffieneproductivityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.graphics.Typeface
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val log_btn : TextView = findViewById(R.id.log_a_drink_btn)
        val hist_btn : TextView = findViewById(R.id.view_history_btn)

        val typeface = Typeface.createFromAsset(assets, "Pacifico.ttf")

        log_btn.setTypeface(typeface)
        hist_btn.setTypeface(typeface)

        log_a_drink_btn.setOnClickListener {
            val logIntent = Intent(this,LogDrinkActivity::class.java)
            startActivity(logIntent)
        }

        view_history_btn.setOnClickListener {
            val histIntent = Intent(this,ViewHistoryActivity::class.java)
            startActivity(histIntent)
        }

        settings_btn.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
