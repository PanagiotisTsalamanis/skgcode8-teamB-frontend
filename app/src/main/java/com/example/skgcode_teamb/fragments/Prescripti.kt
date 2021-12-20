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
import com.example.skgcode_teamb.adapters.PrescriptionAdapter
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.Hospitals
import com.example.skgcode_teamb.models.Prescription
import com.example.skgcode_teamb.storage.SessionManager
import com.example.skgcode_teamb.utils.DateFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Prescripti : Fragment(), PrescriptionAdapter.OnItemClickListener {

    private var hospitalNames = arrayListOf<String>()
    private var hospitalEmails = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_prescripti, container, false)

        // Set fragment title
        (activity as HomePa).supportActionBar?.title = "Prescriptions history"

        // Define session manager
        val sessionManager = SessionManager(this.context)

        // Function for
        fun apiPrescriptions() {

            val apiCall = RetrofitClient.getRetrofitInstance.userPrescriptions(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Prescription>> {
                override fun onResponse(
                    call: Call<List<Prescription>>,
                    response: Response<List<Prescription>>
                ) {
                    val responseBody = response.body()!!.sortedByDescending { it.date }

                    val recView : RecyclerView = view!!.findViewById(R.id.Prescription_Recycler_View)

                    recView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = PrescriptionAdapter(responseBody, this@Prescripti)
                    }

                }

                override fun onFailure(call: Call<List<Prescription>>, t: Throwable) {
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


        apiPrescriptions()
        apiHospitals()


        return view



    }

    override fun OnItemClick(prescription: Prescription) {

        val sessionManager = SessionManager(this.context)

        val formattedDate = DateFormatter().apiDateToAppDate(prescription.date)

        val firstName = sessionManager.getUserDetails().firstName
        val lastName = sessionManager.getUserDetails().lastName
        val healthIdNumber = sessionManager.getUserDetails().healthIdNumber

        val emailSubject = "Prescription from $firstName $lastName"
        val userDetails = "First name: $firstName \n Last name: $lastName \n Health ID Number: $healthIdNumber"
        val prescriptionDetails = "Issue Date: $formattedDate" +
                "\n" + "Doctor: ${prescription.doctor.firstName} ${prescription.doctor.lastName}" +
                "\n" + "Drug code: ${prescription.drugCode}" +
                "\n" + "Title: ${prescription.title}" +
                "\n" + "Description: ${prescription.description}"

        val emailBody = "PATIENT DETAILS" + "\n" + userDetails + "\n" + "\n" + "PRESCRIPTION DETAILS" + "\n" + prescriptionDetails


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