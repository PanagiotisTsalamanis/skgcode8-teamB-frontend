package com.example.skgcode_teamb.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.storage.SessionManager

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sessionManager = SessionManager(this)

        val buttonView1 = findViewById<Button>(R.id.LogoutBtn)

        buttonView1.setOnClickListener {
            // Clear session details
            sessionManager.clearSession()

            // After clear session details, redirect user to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)

            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new flag to start new Activity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            startActivity(intent)
            finish()
        }


        val health_id = sessionManager.getUserDetails().healthIdNumber
        val first_name = sessionManager.getUserDetails().firstName
        val last_name = sessionManager.getUserDetails().lastName
        val email = sessionManager.getUserDetails().email
        val phone_number = sessionManager.getUserDetails().phoneNumber
        val birth_date = sessionManager.getUserDetails().birthDate
        val blood_type = sessionManager.getUserDetails().bloodType
        val family_doctor_id = sessionManager.getUserDetails().familyDoctorId


        val healthId_TextView = findViewById<TextView>(R.id.textView4)
        healthId_TextView.setText(health_id)

        val firstName_TextView = findViewById<TextView>(R.id.textView5)
        firstName_TextView.setText(first_name)

        val lastName_TextView = findViewById<TextView>(R.id.textView6)
        lastName_TextView.setText(last_name)

        val email_TextView = findViewById<TextView>(R.id.textView7)
        email_TextView.setText(email)

        val phone_TextView = findViewById<TextView>(R.id.textView8)
        phone_TextView.setText(phone_number)

        val birth_TextView = findViewById<TextView>(R.id.textView9)
        birth_TextView.setText(birth_date)

        val blood_TextView = findViewById<TextView>(R.id.textView10)
        blood_TextView.setText(blood_type)

        val familyd_TextView = findViewById<TextView>(R.id.textView11)
        familyd_TextView.setText(family_doctor_id)



        /**
         * Prescriptions button
         */
        val ButtonView10 = findViewById<Button>(R.id.button)
        ButtonView10.setOnClickListener {
            startActivity(Intent(this, Prescriptions::class.java))
        }




        // Toast.makeText(this@ProfileActivity, "Welco1me $first_name $last_name", Toast.LENGTH_SHORT).show()



        /*
            val retrofitbuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)

            val retrofitData = retrofitbuilder.userProfile(token = "${sessionManager.fetchAuthToken()}")



            retrofitData.enqueue(object: Callback<ProfileResponse> {
                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.d("ProfileActivity","Failed Login: ${t.message}")
                }

                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    val userdata = response.body()

                    Toast.makeText(this@ProfileActivity, "Welcome $userdata", Toast.LENGTH_SHORT).show()

                }

            }) */

    }

}