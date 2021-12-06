package com.example.skgcode_teamb.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.models.Prescription
import java.text.SimpleDateFormat
import java.util.*


class PrescriptionAdapter(
    val prescriptions: List<Prescription>,
    val listener: OnItemClickListener):
    RecyclerView.Adapter<PrescriptionAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_prescription, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prescriptions.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(prescriptions[position])
    }

    inner class MoviesViewHolder(itemView : View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val date:TextView = itemView.findViewById(R.id.PerIssueDate)
        private val title:TextView = itemView.findViewById(R.id.PerTittle)
        private val description:TextView = itemView.findViewById(R.id.PerDescription)
        private val btnShare:Button = itemView.findViewById(R.id.PerShareBtn)

        fun bind(prescription: Prescription) {
            // Format Date
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.UK)
            val formattedDate = formatter.format(parser.parse(prescription.date))

            date.text = formattedDate
            title.text = prescription.title
            description.text = prescription.description

            val perSum = "Issue Date: ${date.text}" + "\n" + "Title: ${title.text}" + "\n" + "Description: ${description.text}"
        }

        init {
            btnShare.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(prescriptions[position])
            }
        }

    }

    interface OnItemClickListener {
        fun OnItemClick(prescription: Prescription)
    }

}