package com.example.skgcode_teamb.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.afollestad.vvalidator.form
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.activities.HomePa
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.Hospitals
import com.example.skgcode_teamb.models.ScheduleAppRequest
import com.example.skgcode_teamb.models.ScheduleAppResponse
import com.example.skgcode_teamb.storage.SessionManager
import com.example.skgcode_teamb.utils.DateFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SceduleApp : Fragment() {

    private var availableHours = arrayListOf<String>()
    private var availableHoursFormatted = arrayListOf<String>()

    private var hospitalNames = arrayListOf<String>()
    private var hospitalIds = arrayListOf<String>()
    private var hospitalDepartments = arrayListOf<List<String>>()
    private var pickTime = arrayListOf<ArrayList<String>>()
    private var itemPosition : Int? = null
    private var itemPosition1 : Int? = null

    private var readyToSchedule : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scedule_app, container, false)

        // Set fragment title
        (activity as HomePa).supportActionBar?.title = "Schedule Appointment"

        // Define session manager
        val sessionManager = SessionManager(this.context)


        var hospitalId = ""
        var hospitalNameTest = ""
        var department = ""
        var date = ""
        var dateTime = ""

        // TextInput
        val HospitalInputLayout : TextInputLayout = view.findViewById(R.id.hospitalInputLayout)
        val DepartmentsInputLayout : TextInputLayout = view.findViewById(R.id.departmentInputLayout)
        val DateList : TextInputEditText = view.findViewById(R.id.dateList)
        val timeList : TextInputLayout = view.findViewById(R.id.timeInputLayout)

        // Edit text
        val hospitalIdText : AutoCompleteTextView = view.findViewById(R.id.hospitalList)
        val departmentText : AutoCompleteTextView = view.findViewById(R.id.departmentList)
        val dateTimeText : AutoCompleteTextView = view.findViewById(R.id.timeList)
        val dateText : TextInputEditText = view.findViewById(R.id.dateList)


        // Get hospitals
        fun apiHospitals() {

            val apiCall = RetrofitClient.getRetrofitInstance.hospitals(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Hospitals>?> {
                override fun onResponse(
                    call: Call<List<Hospitals>?>,
                    response: Response<List<Hospitals>?>

                ) {
                    val responseBody = response.body()!!


                    responseBody.forEach{
                        hospitalIds.add(it._id)
                        hospitalNames.add(it.name)
                        hospitalDepartments.add(it.departments)
                    }

                    Log.d("ScheduleFragment", "Hospital Names: $hospitalNames")
                    Log.d("ScheduleFragment", "Hospital Departments: $hospitalDepartments")


                }

                override fun onFailure(call: Call<List<Hospitals>?>, t: Throwable) {
                    Log.d("SceduleActivity", "onFailure:${t.message}")
                }
            })
        }


        apiHospitals()


        // Hospital & Department Dropdowns
        val hospitalName = ArrayAdapter(requireContext(), R.layout.list_item, hospitalNames)
        (HospitalInputLayout.editText as? AutoCompleteTextView)?.setAdapter(hospitalName)

        val departmentsEditText : AutoCompleteTextView = view.findViewById(R.id.hospitalList)

        departmentsEditText.setOnItemClickListener { parent, view, position, id ->
            itemPosition = position
            Log.d("ScheduleFragment", "Hospital Selection: $itemPosition")

            DepartmentsInputLayout.setEnabled(true)

            val department = ArrayAdapter(requireContext(), R.layout.list_item, hospitalDepartments[itemPosition!!])
            (DepartmentsInputLayout.editText as? AutoCompleteTextView)?.setAdapter(department)

        }


        val timeEditText : AutoCompleteTextView = view.findViewById(R.id.timeList)

        timeEditText.setOnItemClickListener{parent, view, position, id ->
            itemPosition1 = position
        }



        // Datepicke
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        DateList.inputType = InputType.TYPE_NULL

        DateList.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                datePicker.show(childFragmentManager, "tag")
            }
        }

        DateList.setOnClickListener {
            datePicker.show(childFragmentManager, "tag")
        }

        var datePickerSelection = null as Long?

        datePicker.addOnPositiveButtonClickListener {
            datePickerSelection = datePicker.selection

            val datePickerFormattedSelection = DateFormatter().milliSecondsToAppDate(datePicker.selection!!)
            DateList.setText(datePickerFormattedSelection)

        }

        // Search Availability Request

        fun searchAppointment() {

            val apiCall = RetrofitClient.getRetrofitInstance.searchAppointment(
                token = "JWT ${sessionManager.fetchAuthToken()}",
                hospitalId, department, date
            )

            apiCall.enqueue(object : Callback<List<String>?> {
                override fun onResponse(
                    call: Call<List<String>?>,
                    response: Response<List<String>?>
                ) {

                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        Log.d("BookAppointment", "Success: $statusCode")
                        Toast.makeText(requireContext(), "You can choose time", Toast.LENGTH_LONG).show()

                        val responseBody = response.body()!!

                        Log.d("ScheduleFragment", "Availability: $responseBody")

                        timeList.setEnabled(true)

                        responseBody.forEach{
                            availableHours.add(it)
                        }

                        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        val formatter = SimpleDateFormat("h:mm a")
                        formatter.timeZone = TimeZone.getTimeZone("UTC")

                        //val singleChange = formatter.format(parser.parse(availableHours[2]))
                        //Log.d("ScheduleAppointment", "Single change: ${availableHours[2]}, $singleChange")

                        responseBody.forEach {
                            availableHoursFormatted.add(formatter.format(parser.parse(it)))
                        }


                        Log.d("ScheduleFragment", "Formatted time: $availableHoursFormatted")



                        Log.d("ScheduleFragment", "Availability: $availableHours")

                        val HoursInputLayout : TextInputLayout = view.findViewById(R.id.timeInputLayout)

                        val items1 = availableHoursFormatted
                        val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, items1)
                        (HoursInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter1)

                        readyToSchedule = true

                    } else {
                        Log.d("BookAppointment", "Success or not: $statusCode")
                        Toast.makeText( requireContext(), "No time available", Toast.LENGTH_LONG).show()
                    }


                }

                override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                    Log.d("BookAppointment", "Failed Time: ${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }


        // Schedule appointment
        fun scheduleAppointment() {

            val apiCall = RetrofitClient.getRetrofitInstance.scheduleAppointment(
                token = "JWT ${sessionManager.fetchAuthToken()}",
                ScheduleAppRequest(hospitalId,department,dateTime)
            )

            apiCall.enqueue(object : Callback<ScheduleAppResponse?> {
                override fun onResponse(
                    call: Call<ScheduleAppResponse?>,
                    response: Response<ScheduleAppResponse?>
                ) {
                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        val responseBody = response.body()!!

                        Toast.makeText(requireContext(), "Your appointment has been scheduled successfully.", Toast.LENGTH_LONG).show()

                        Log.d("ScheduleAppointment", "Success Appointment: $responseBody")

                    }
                }

                override fun onFailure(call: Call<ScheduleAppResponse?>, t: Throwable) {
                    Log.d("BookAppointment", "Failed Time: ${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }

        /*
        val submitButton : Button = view.findViewById(R.id.BookAppointment)

        submitButton.setOnClickListener {
            if (readyToSchedule == false) {
                timeList.error = "Please select time."
            }
        }
         */



        form {
            inputLayout(view.findViewById(R.id.hospitalInputLayout)) {
                isNotEmpty().description("Please select a hospital.")
            }

            inputLayout(view.findViewById(R.id.departmentInputLayout)) {
                isNotEmpty().description("Please select a department.")
            }

            inputLayout(view.findViewById(R.id.dateListInputLayout)) {
                isNotEmpty().description("Please select a date.")
            }


            /*
            inputLayout(view.findViewById(R.id.timeInputLayout)) {
                isNotEmpty().description("Please select time.")
            }
             */


            submitWith(view.findViewById(R.id.BookAppointment)) { result ->

                if (readyToSchedule == false) {

                    if (itemPosition != null) {
                        hospitalId = hospitalIds[itemPosition!!]
                    }

                    hospitalNameTest = hospitalIdText.text.toString().trim()
                    department = departmentText.text.toString().trim()

                    val parser = SimpleDateFormat("MMMM dd, yyyy", Locale.UK)
                    val formatter = SimpleDateFormat("yyyy-MM-dd")
                    val formattedDate = formatter.format(parser.parse(dateText.text.toString()))

                    date = formattedDate



                    Log.d(
                        "ScheduleFragment",
                        "User search: $hospitalId, $hospitalNameTest, $department, $date"
                    )

                    searchAppointment()
                } else {
                    Log.d("ScheduleAppointment", "Ready to Schedule")

                    hospitalId = hospitalIds[itemPosition!!]
                    department = departmentText.text.toString().trim()
                    dateTime = availableHours[itemPosition1!!]

                    Log.d("ScheduleAppointment", "Schedule Selectio: $hospitalId, $department, $dateTime")
                    Log.d("ScheduleAppointment", "Available hours: $availableHours")
                    Log.d("ScheduleAppointment", "Available hours formatted: $availableHoursFormatted")

                    scheduleAppointment()


                }

            }
        }












        /* Test Test Test
        var hospitalId = ""
        var department = ""
        var date = ""

        val hospitalIdText : TextInputEditText = view.findViewById(R.id.textField1Text)
        val departmentText : TextInputEditText = view.findViewById(R.id.textField2Text)
        val dateText : TextInputEditText = view.findViewById(R.id.textField3Text)

        fun searchAppointment() {

            val apiCall = RetrofitClient.getRetrofitInstance.searchAppointment(
                token = "JWT ${sessionManager.fetchAuthToken()}",
                hospitalId, department, date
            )

            apiCall.enqueue(object : Callback<List<String>?> {
                override fun onResponse(
                    call: Call<List<String>?>,
                    response: Response<List<String>?>
                ) {

                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        val responseBody = response.body()!!

                        Log.d("ScheduleFragment", "Availability: $responseBody")


                        availableHours = responseBody.toTypedArray()


                        Log.d("ScheduleFragment", "Availability: $availableHours")

                        val HoursInputLayout : TextInputLayout = view.findViewById(R.id.HoursInput)

                        val items1 = availableHours.toList()
                        val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, items1)
                        (HoursInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter1)
                    }




                }

                override fun onFailure(call: Call<List<String>?>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }


        form {
            submitWith(view.findViewById(R.id.button2)) { result ->
                hospitalId = hospitalIdText.text.toString().trim()
                department = departmentText.text.toString().trim()
                date = dateText.text.toString().trim()

                searchAppointment()

            }
        }

         */




        return view


    }


}