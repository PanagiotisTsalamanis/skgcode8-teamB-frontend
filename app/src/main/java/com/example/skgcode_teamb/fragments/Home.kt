package com.example.skgcode_teamb.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.databinding.FragmentHomeBinding


class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val nextBtn1 : Button = view.findViewById(R.id.btnPrescriptions)
        nextBtn1.setOnClickListener{
            val fragment = Prescripti()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

        val nextBtn2 : Button = view.findViewById(R.id.btnAppointments)
        nextBtn2.setOnClickListener{
            val fragment = Appointments()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

        val nextBtn3 : Button = view.findViewById(R.id.btnDiagnosesHistory)
        nextBtn3.setOnClickListener{
            val fragment = Diagnoses()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

        val nextBtn4 : Button = view.findViewById(R.id.btnScheduleAppointment)
        nextBtn4.setOnClickListener{
            val fragment = SceduleApp()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

        val nextBtn5 : Button = view.findViewById(R.id.btnUserProfile)
        nextBtn5.setOnClickListener{
            val fragment = UserProfile()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

        return view
    }

}