package com.example.skgcode_teamb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.models.Diagnosis
import com.example.skgcode_teamb.models.Prescription
import com.example.skgcode_teamb.utils.DateFormatter

class DiagnosisAdapter(
    val diagnosis: List<Diagnosis>,
    val listener: OnItemClickListener):
    RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_diagnosis, parent, false)
        return DiagnosisViewHolder(view)
    }

    override fun getItemCount(): Int {
        return diagnosis.size
    }

    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        return holder.bind(diagnosis[position])
    }

    inner class DiagnosisViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val date: TextView = itemView.findViewById(R.id.DiagnosisDate)
        private val examination: TextView = itemView.findViewById(R.id.DiagnosisExam)
        private val results: TextView = itemView.findViewById(R.id.DiagnosisResult)
        private val diagnosi: TextView = itemView.findViewById(R.id.DiagnosisDiagnosis)
        private val doctor: TextView = itemView.findViewById(R.id.DiagnosisDoctor)
        private val btnShare: Button = itemView.findViewById(R.id.DiagnosisShareBtn)

        fun bind(diagnosis: Diagnosis) {

            date.text = DateFormatter().apiDateToAppDate(diagnosis.date)
            examination.text = diagnosis.examination
            results.text = diagnosis.results
            diagnosi.text = diagnosis.diagnosis
            doctor.text = diagnosis.doctor.firstName + " " + diagnosis.doctor.lastName
        }

        init {
            btnShare.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(diagnosis[position])
            }
        }

    }

    interface OnItemClickListener {
        fun OnItemClick(diagnosis: Diagnosis)
    }

}