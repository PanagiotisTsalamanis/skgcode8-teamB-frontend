package com.example.skgcode_teamb.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.activities.HomePa
import com.example.skgcode_teamb.adapters.AppointmentAdapter
import com.example.skgcode_teamb.adapters.PrescriptionAdapter
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.Appointments
import com.example.skgcode_teamb.storage.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Appointments : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointments, container, false)

        // Set fragment title
        (activity as HomePa).supportActionBar?.title = "Appointments history"

        // Define session manager
        val sessionManager = SessionManager(this.context)

        fun apiAppointments() {

            val apiCall = RetrofitClient.getRetrofitInstance.userAppointments(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Appointments>?> {
                override fun onResponse(
                    call: Call<List<Appointments>?>,
                    response: Response<List<Appointments>?>
                ) {

                    val responseBody = response.body()!!.sortedByDescending { it.date }

                    val recView : RecyclerView = view!!.findViewById(R.id.Appointment_Recycler_View)

                    recView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = AppointmentAdapter(responseBody)
                    }

                }

                override fun onFailure(call: Call<List<Appointments>?>, t: Throwable) {
                    Log.d("PrescriptionsActivity", "onFailure: ${t.message}")
                }
            })

        }


        apiAppointments()

        return view


    }


}