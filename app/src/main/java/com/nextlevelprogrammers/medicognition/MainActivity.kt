@file:Suppress("DEPRECATION")

package com.nextlevelprogrammers.medicognition

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs: SharedPreferences = getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("firstTime", true)) {
            // if it's not the first time launching the app, navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        setContentView(R.layout.activity_main)

        val login = findViewById<Button>(R.id.button)

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}