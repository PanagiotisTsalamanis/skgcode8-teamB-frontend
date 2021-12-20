package com.example.skgcode_teamb.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.vvalidator.util.hide
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.models.Appointments
import com.example.skgcode_teamb.utils.DateFormatter
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker.todayInUtcMilliseconds

class AppointmentAdapter(val appointments: List<Appointments>):
    RecyclerView.Adapter<AppointmentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_appointments, parent, false)
        return AppointmentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        return holder.bind(appointments[position])
    }

}

class AppointmentsViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
    private val date: TextView = itemView.findViewById(R.id.AppointmentDate)
    private val time: TextView = itemView.findViewById(R.id.AppointmentTime)
    private val hospital: TextView = itemView.findViewById(R.id.AppointmentHospital)
    private val department: TextView = itemView.findViewById(R.id.AppointmentDepartment)
    private val doctorLabel: TextView = itemView.findViewById(R.id.AppointmentDoctorLabel)
    private val doctor: TextView = itemView.findViewById(R.id.AppointmentDoctor)
    private val secondRow: LinearLayout = itemView.findViewById(R.id.AppointmentsSecondRow)
    private val clip: Chip = itemView.findViewById(R.id.chip)


    fun bind(appointments: Appointments) {

        date.text = DateFormatter().apiDateToAppDate(appointments.date)
        time.text = DateFormatter().apiTimeToAppTime(appointments.date)
        hospital.text = if (appointments.hospital != null) {
            appointments.hospital.name
        } else {
            "No hospital"
        }
        department.text = if (appointments.hospital != null) {
            appointments.department
        } else {
            "No department"
        }
        doctorLabel.text = if (appointments.hospital != null) {
            "Hospital doctor"
        } else {
            "Family doctor"
        }
        doctor.text = appointments.doctor.firstName + " " + appointments.doctor.lastName

        secondRow.visibility = if (appointments.hospital != null) {
            View.VISIBLE
        } else {
            View.GONE
        }

        clip.text = if (DateFormatter().apiDateToMilliSeconds(appointments.date) < todayInUtcMilliseconds()) {
            "Completed"
        } else {
            "Upcoming"
        }


        if (DateFormatter().apiDateToMilliSeconds(appointments.date) < todayInUtcMilliseconds()) {
            clip.setChipBackgroundColorResource(R.color.green)
        } else {
            clip.setChipBackgroundColorResource(R.color.red)
        }



    }

}