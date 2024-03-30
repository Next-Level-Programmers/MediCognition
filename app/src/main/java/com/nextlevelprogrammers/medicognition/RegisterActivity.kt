package com.nextlevelprogrammers.medicognition

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val registerBtn = findViewById<Button>(R.id.register)
        registerBtn.setOnClickListener {
            registerUser()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@gmail\\.com")
        return emailRegex.matches(email)
    }

    private fun registerUser() {
        val emailEditText = findViewById<EditText>(R.id.editTextEmailRegister)
        val passwordEditText = findViewById<EditText>(R.id.editTextPasswordRegister)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmPasswordRegister)

        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email format. Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check password length
        if (password.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show()
            return
        }

        // Verify password and confirm password match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if email is already registered
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList()
                    if (signInMethods.isNotEmpty()) {
                        // Email is already registered
                        Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show()
                    } else {
                        // Proceed with registration
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { registrationTask ->
                                if (registrationTask.isSuccessful) {
                                    // Registration success, send email verification
                                    val user = auth.currentUser
                                    user?.sendEmailVerification()
                                        ?.addOnCompleteListener { emailTask ->
                                            if (emailTask.isSuccessful) {
                                                // Email verification sent successfully
                                                Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                                                // Redirect the user to the login screen
                                                startActivity(Intent(this, LoginActivity::class.java))
                                                finish()
                                            } else {
                                                // Failed to send verification email
                                                Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                } else {
                                    // If registration fails, display a message to the user.
                                    // You can handle different error cases here
                                    // For example, check registrationTask.exception for specific errors
                                    // Also, update UI accordingly
                                }
                            }
                    }
                } else {
                    // Error occurred while checking email registration status
                    // Handle the error as needed
                    Toast.makeText(this, "Error checking email registration status", Toast.LENGTH_SHORT).show()
                }
            }

    }
}