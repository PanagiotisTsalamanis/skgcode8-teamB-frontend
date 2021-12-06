package com.example.skgcode_teamb.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.afollestad.vvalidator.form
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.LoginRequest
import com.example.skgcode_teamb.models.RegisterRequest
import com.example.skgcode_teamb.utils.InternetConnectivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /**
         * Define register input variables
         */
        val healthIdTextView = findViewById<TextInputEditText>(R.id.HealthIdInputEditText)
        val passwordTextView = findViewById<TextInputEditText>(R.id.PasswordInputEditText)
        val emailTextView = findViewById<TextInputEditText>(R.id.EmailInputEditText)
        var healthIdNumber = ""
        var password = ""
        var email = ""


        /**
         * Create function for Register Api Call
         */
        fun apiRegister() {

            val apiCall = RetrofitClient.getRetrofitInstance.userRegister(RegisterRequest(healthIdNumber, email, password))

            apiCall.enqueue(object: Callback<Html> {
                override fun onFailure(call: Call<Html>, t: Throwable) {
                    Log.d("RegisterActivity","Failed Register: ${t.message}")
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Html>, response: Response<Html>) {
                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        Log.d("RegisterActivity","Success: $statusCode")
                        Toast.makeText(this@RegisterActivity, "Your account has been created successfully.", Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("RegisterActivity","Success or not: $statusCode")
                        Toast.makeText(this@RegisterActivity, "User with this health ID already exists.", Toast.LENGTH_LONG).show()
                    }
                }

            })

        }



        /**
         * Validate register form with vvalidator library
         */
        form {
            inputLayout(R.id.HealthIdInput) {
                isNotEmpty().description("Please enter your health ID number.")
                length().exactly(11).description("Health ID must be exactly 11 digits.")
            }
            inputLayout(R.id.EmailInput) {
                isNotEmpty().description("Please enter your email address.")
                isEmail().description("Please enter a valid email address.")
            }
            inputLayout(R.id.PasswordInput) {
                isNotEmpty().description("Please enter your password.")
            }
            submitWith(R.id.SubmitButton) { result ->
                // Button was clicked and form is completely valid!
                healthIdNumber = healthIdTextView.text.toString().trim()
                password = passwordTextView.text.toString().trim()
                email = emailTextView.text.toString().trim()

                apiRegister()

            }
        }




    }
}