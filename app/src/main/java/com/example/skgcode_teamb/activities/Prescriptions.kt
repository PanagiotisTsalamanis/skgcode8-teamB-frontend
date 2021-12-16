package com.example.skgcode_teamb.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.adapters.PrescriptionAdapter
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.Prescription
import com.example.skgcode_teamb.storage.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Prescriptions : AppCompatActivity(), PrescriptionAdapter.OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescriptions)

        /**
         * Define session manager
         */
        val sessionManager = SessionManager(this)






        fun apiPrescriptions() {

            val apiCall = RetrofitClient.getRetrofitInstance.userPrescriptions(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Prescription>> {
                override fun onResponse(
                    call: Call<List<Prescription>>,
                    response: Response<List<Prescription>>
                ) {
                    val responseBody = response.body()!!

                    Log.d("PrescriptionActivity", "$responseBody")


                    val recView = findViewById<RecyclerView>(R.id.Prescription_Recycler_View)

                    recView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@Prescriptions)
                        adapter = PrescriptionAdapter(response.body()!!, this@Prescriptions)
                    }




                }

                override fun onFailure(call: Call<List<Prescription>>, t: Throwable) {
                    Log.d("PrescriptionsActivity", "onFailure: ${t.message}")
                }
            })


        }


        apiPrescriptions()




    }

    override fun OnItemClick(prescription: Prescription) {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.UK)
        val formattedDate = formatter.format(parser.parse(prescription.date))

        val firstName = SessionManager(this).getUserDetails().firstName
        val lastName = SessionManager(this).getUserDetails().lastName
        val amka = SessionManager(this).getUserDetails().healthIdNumber


        val emailSubject = "Prescription from " + firstName + " " + lastName
        val emailBody = "Issue Date: $formattedDate" + "\n" + "Title: ${prescription.title}" + "\n" + "Description: ${prescription.description}"

        //Toast.makeText(this, "$emailBody", Toast.LENGTH_LONG).show()


        val hospitalNames = arrayOf("Saint Paul Hospital", "Saint Dimitrios Hospital", "Ippokratio Hospital", "AHEPA Hospital", "Gennimatas Hospital")
        val hospitalEmails = arrayOf("saintpaulhospital@example.com", "saintdimhospital@example.com", "ippokratiohospital@example.com", "axepa@example.com", "gennimatas@example.com")
        val checkedItem = 0



        MaterialAlertDialogBuilder(this)
            .setTitle("Select hospital")
            .setItems(hospitalNames) { dialog, which ->
                // Respond to item chosen
                val emailAddress = hospitalEmails[which].split(",".toRegex()).toTypedArray()

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, emailAddress)
                    putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Required app is not installed", Toast.LENGTH_SHORT).show()
                }



            }
            .show()



    }
}