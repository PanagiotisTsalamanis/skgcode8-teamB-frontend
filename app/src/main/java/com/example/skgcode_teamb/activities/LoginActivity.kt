package com.example.skgcode_teamb.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.afollestad.vvalidator.form
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.storage.SessionManager
import com.example.skgcode_teamb.models.LoginRequest
import com.example.skgcode_teamb.models.LoginResponse
import com.example.skgcode_teamb.utils.InternetConnectivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * Define session manager
         */
        val sessionManager = SessionManager(this)

        /**
         * Define login input variables
         */
        val healthIdTextView = findViewById<TextInputEditText>(R.id.HealthIdInputEditText)
        val passwordTextView = findViewById<TextInputEditText>(R.id.PasswordInputEditText)
        var healthIdNumber = ""
        var password = ""

        /**
         * Create function for Login Api Call
         */
        fun apiLogin() {

            val apiCall = RetrofitClient.getRetrofitInstance.userLogin(LoginRequest(healthIdNumber,password))

            apiCall.enqueue(object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("LoginActivity","Failed Login: ${t.message}")
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        // Create login session
                        sessionManager.createSession(response.body()!!)

                        // Finish login activity and redirect user to welcome screen
                        val intent = Intent(this@LoginActivity, HomePa::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "Health ID number or password is invalid.", Toast.LENGTH_LONG).show()
                    }

                }

            })
        }

        /**
         * Validate login form with vvalidator library
         */
        form {
            inputLayout(R.id.HealthIdInput) {
                isNotEmpty().description("Please enter your health ID number.")
            }
            inputLayout(R.id.PasswordInput) {
                isNotEmpty().description("Please enter your password.")
            }
            submitWith(R.id.SubmitButton) { result ->
                // Button was clicked and form is completely valid!
                healthIdNumber = healthIdTextView.text.toString().trim()
                password = passwordTextView.text.toString().trim()

                // Check if phone is connected to internet
                if (InternetConnectivity().checkForInternet(this@LoginActivity)) {
                    // If phone is connected to internet, make api call
                    apiLogin()
                } else {
                    // If phone is not connected to internet, show alert dialog
                    MaterialAlertDialogBuilder(this@LoginActivity)
                        .setMessage("You are not connected to the Internet.")
                        .setNegativeButton("Cancel") { dialog, which ->
                            // Respond to negative button press
                        }
                        .setPositiveButton("Connect") { dialog, which ->
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                        .show()
                }

            }
        }

        /**
         * Register button
         */
        val registerTextView = findViewById<TextView>(R.id.RegisterTextView)
        registerTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }

    override fun onStart() {
        super.onStart()

        // If user is already logged in, redirect them to Profile Activity
        if(SessionManager(this).isLoggedIn) {
            val intent = Intent(this@LoginActivity, HomePa::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}