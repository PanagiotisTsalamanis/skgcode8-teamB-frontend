package com.example.skgcode_teamb.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.activities.HomePa
import com.example.skgcode_teamb.adapters.DiagnosisAdapter
import com.example.skgcode_teamb.adapters.PrescriptionAdapter
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.Diagnosis
import com.example.skgcode_teamb.models.Hospitals
import com.example.skgcode_teamb.models.Prescription
import com.example.skgcode_teamb.storage.SessionManager
import com.example.skgcode_teamb.utils.DateFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Diagnoses : Fragment(), DiagnosisAdapter.OnItemClickListener {

    private var hospitalNames = arrayListOf<String>()
    private var hospitalEmails = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_diagnoses, container, false)

        // Set fragment title
        (activity as HomePa).supportActionBar?.title = "Diagnoses history"

        // Define session manager
        val sessionManager = SessionManager(this.context)

        fun apiDiagnoses() {

            val apiCall = RetrofitClient.getRetrofitInstance.userDiagnoses(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Diagnosis>?> {
                override fun onResponse(
                    call: Call<List<Diagnosis>?>,
                    response: Response<List<Diagnosis>?>
                ) {
                    val responseBody = response.body()!!.sortedByDescending { it.date }

                    val recView : RecyclerView = view!!.findViewById(R.id.Diagnoses_Recycler_View)

                    recView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = DiagnosisAdapter(responseBody, this@Diagnoses)
                    }
                }

                override fun onFailure(call: Call<List<Diagnosis>?>, t: Throwable) {
                    Log.d("PrescriptionsActivity", "onFailure: ${t.message}")
                }
            })

        }


        // Function for
        fun apiHospitals() {

            val apiCall = RetrofitClient.getRetrofitInstance.hospitals(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Hospitals>> {
                override fun onResponse(
                    call: Call<List<Hospitals>>,
                    response: Response<List<Hospitals>>
                ) {

                    val responseBody = response.body()!!

                    responseBody.forEach{
                        hospitalNames.add(it.name)
                        hospitalEmails.add(it.email)
                    }


                }

                override fun onFailure(call: Call<List<Hospitals>?>, t: Throwable) {
                    Log.d("PrescriptionsActivity", "onFailure: ${t.message}")
                }
            })



        }


        apiDiagnoses()
        apiHospitals()

        return view
    }

    override fun OnItemClick(diagnosis: Diagnosis) {

        val sessionManager = SessionManager(this.context)

        val formattedDate = DateFormatter().apiDateToAppDate(diagnosis.date)

        val firstName = sessionManager.getUserDetails().firstName
        val lastName = sessionManager.getUserDetails().lastName
        val healthIdNumber = sessionManager.getUserDetails().healthIdNumber

        val emailSubject = "Diagnosis from $firstName $lastName"
        val userDetails = "First name: $firstName \n Last name: $lastName \n Health ID Number: $healthIdNumber"
        val prescriptionDetails = "Date: $formattedDate" +
                "\n" + "Doctor: ${diagnosis.doctor.firstName} ${diagnosis.doctor.lastName}" +
                "\n" + "Examination: ${diagnosis.examination}" +
                "\n" + "Results: ${diagnosis.results}" +
                "\n" + "Diagnosis: ${diagnosis.diagnosis}"

        val emailBody = "PATIENT DETAILS" + "\n" + userDetails + "\n" + "\n" + "DIAGNOSIS DETAILS" + "\n" + prescriptionDetails


        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Select hospital")
            .setItems(hospitalNames.toTypedArray()) { dialog, which ->
                // Respond to item chosen
                val emailAddress = hospitalEmails.toTypedArray()[which].split(",".toRegex()).toTypedArray()

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, emailAddress)
                    putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    putExtra(Intent.EXTRA_TEXT, emailBody)
                }

                if (intent.resolveActivity(this.requireContext().packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this.context, "Required app is not installed", Toast.LENGTH_SHORT).show()
                }



            }
            .show()


    }

}