package com.nextlevelprogrammers.medicognition

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)

        // Check if the user is already logged in
        if (isLoggedIn()) {
            navigateToHome()
            return
        }

        val register = findViewById<TextView>(R.id.register1)
        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val login = findViewById<Button>(R.id.signInButton)
        login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = findViewById<TextView>(R.id.editTextEmail).text.toString()
        val password = findViewById<TextView>(R.id.editTextPassword).text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Set the logged-in flag to true
                        setLoggedIn(true)
                        // Navigate to home activity
                        navigateToHome()
                    } else {
                        // User is signed in but email is not verified
                        // Display a message to the user and prompt them to verify their email
                        Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle login failure
                    Toast.makeText(this, "Login failed. Please check your credentials and try again", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }
}