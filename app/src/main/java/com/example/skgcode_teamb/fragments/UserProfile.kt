package com.example.skgcode_teamb.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.afollestad.vvalidator.form
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.storage.SessionManager
import com.example.skgcode_teamb.utils.DateFormatter
import com.example.skgcode_teamb.utils.compareWith
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.widget.*
import com.example.skgcode_teamb.activities.HomePa
import com.example.skgcode_teamb.api.RetrofitClient
import com.example.skgcode_teamb.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class UserProfile : Fragment() {

    private var familyDoctorsIds = arrayListOf<String>()
    private var familyDoctorsFirstNames = arrayListOf<String>()
    private var familyDoctorsLastNames = arrayListOf<String>()
    private var familyDoctorsFullNames = arrayListOf<String>()
    private var itemPosition : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Set fragment title
        (activity as HomePa).supportActionBar?.title = "Patient profile"

        // Define session manager
        val sessionManager = SessionManager(this.context)

        // Define date formatter
        val dateFormatter = DateFormatter()

        // Input variables for Api Call
        var healthIdNumber = sessionManager.getUserDetails().healthIdNumber
        var firstName = sessionManager.getUserDetails().firstName
        var lastName = sessionManager.getUserDetails().lastName
        var email = sessionManager.getUserDetails().email
        var phoneNumber = sessionManager.getUserDetails().phoneNumber
        var bloodType = sessionManager.getUserDetails().bloodType
        var birthDate = sessionManager.getUserDetails().birthDate
        var familyDoctorId = sessionManager.getUserDetails().familyDoctorId
        var familyDoctorFirstName = sessionManager.getUserDetails().familyDoctorFirstName
        var familyDoctorLastName = sessionManager.getUserDetails().familyDoctorLastName
        var password = ""

        // Target TextInputEditText views for all profile fields
        val healthIdEditText : TextInputEditText = view.findViewById(R.id.HealthIdEditText)
        val firstNameEditText : TextInputEditText = view.findViewById(R.id.FirstNameEditText)
        val lastNameEditText : TextInputEditText = view.findViewById(R.id.LastNameEditText)
        val emailEditText : TextInputEditText = view.findViewById(R.id.EmailEditText)
        val phoneNumberEditText : TextInputEditText = view.findViewById(R.id.PhoneEditText)
        val bloodTypeEditText : AutoCompleteTextView = view.findViewById(R.id.BloodTypeSelect)
        val birthDateEditText : TextInputEditText = view.findViewById(R.id.BirthDateEditText)
        val familyDoctorEditText : AutoCompleteTextView = view.findViewById(R.id.FamilyDoctorSelect)
        val passwordEditText : TextInputEditText = view.findViewById(R.id.PasswordEditText)
        val confPasswordEditText : TextInputEditText = view.findViewById(R.id.ConfirmPasswordEditText)

        // Prefill profile fields with user session data
        // Check if a specific field is existed in user session. Otherwise
        healthIdEditText.setText(healthIdNumber)
        if (firstName != null) firstNameEditText.setText(firstName)
        if (lastName != null) lastNameEditText.setText(lastName)
        emailEditText.setText(email)
        if (phoneNumber != null) phoneNumberEditText.setText(phoneNumber)
        if (bloodType != null) bloodTypeEditText.setText(bloodType)
        if (birthDate != null) {
            val birthDateFormatted = dateFormatter.apiDateToAppDate(birthDate)
            birthDateEditText.setText(birthDateFormatted)
        }
        if (familyDoctorId != null) familyDoctorEditText.setText("$familyDoctorFirstName $familyDoctorLastName")

        // Blood Type select field
        val bloodTypeInputLayout : TextInputLayout = view.findViewById(R.id.BloodTypeInput)

        val items = listOf("O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (bloodTypeInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        // Material Date Picker Dialog
        // Define function for checking if user has added its date of birth. If so, open
        // the date picker with its birth date selected. Otherwise, open the date picker
        // with today's date selected.
        fun getSelectionDate(): Long {
            return if (birthDate == null) {
                MaterialDatePicker.todayInUtcMilliseconds()
            } else {
                dateFormatter.apiDateToMilliSeconds(birthDate!!)
            }
        }

        // Define date picker dialog
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(getSelectionDate())
            .build()

        // Hide Android soft keyboard when birth date EditText is focused
        birthDateEditText.inputType = InputType.TYPE_NULL

        // Open dialog when birth date EditText is focused
        birthDateEditText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                datePicker.show(childFragmentManager, "tag")
            }
        }

        // Open dialog when birth date EditText is clicked
        birthDateEditText.setOnClickListener{
            datePicker.show(childFragmentManager, "tag")
        }

        // Set default value of datePickerSelection to null. This means that the user
        // has not selected any date from the date picker.
        var datePickerSelection = null as Long?

        // Respond to positive button click
        datePicker.addOnPositiveButtonClickListener{
            // User has selected a date from the date picker.
            datePickerSelection = datePicker.selection

            // Format MilliSeconds to Date
            val datePickerFormattedSelection = dateFormatter.milliSecondsToAppDate(datePicker.selection!!)
            birthDateEditText.setText(datePickerFormattedSelection)
        }


        // Create function for Profile Update Api Call
        fun apiProfileUpdate() {

            val apiCall = RetrofitClient.getRetrofitInstance.userProfileUpdate(
                token = "JWT ${sessionManager.fetchAuthToken()}",
                profileUpdateRequest = ProfileUpdateRequest(healthIdNumber, firstName, lastName, email, phoneNumber, birthDate, bloodType, Doctor(familyDoctorId!!, familyDoctorFirstName!!, familyDoctorLastName!!))
            )

            apiCall.enqueue(object : Callback<ProfileUpdateResponse> {
                override fun onResponse(
                    call: Call<ProfileUpdateResponse>,
                    response: Response<ProfileUpdateResponse>
                ) {
                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        // Update session date
                        sessionManager.updateSession(ProfileUpdateRequest(healthIdNumber, firstName, lastName, email, phoneNumber, birthDate, bloodType, Doctor(familyDoctorId!!, familyDoctorFirstName!!, familyDoctorLastName!!)))

                        // Show toast message to user
                        Toast.makeText(requireContext(), "Your profile has been updated successfully.", Toast.LENGTH_LONG).show()

                        // Clear focus from all fields
                        healthIdEditText.clearFocus()
                        firstNameEditText.clearFocus()
                        lastNameEditText.clearFocus()
                        emailEditText.clearFocus()
                        phoneNumberEditText.clearFocus()
                        birthDateEditText.clearFocus()
                        bloodTypeEditText.clearFocus()
                        familyDoctorEditText.clearFocus()

                    } else {
                        Toast.makeText(requireContext(), "${response.body()?.message}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProfileUpdateResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }

        // Create function for Password Update Api Call
        fun apiPasswordUpdate() {

            val apiCall = RetrofitClient.getRetrofitInstance.userPasswordUpdate(
                token = "JWT ${sessionManager.fetchAuthToken()}",
                passwordUpdateRequest = PasswordUpdateRequest(password)
            )

            apiCall.enqueue(object : Callback<ProfileUpdateResponse> {
                override fun onResponse(
                    call: Call<ProfileUpdateResponse>,
                    response: Response<ProfileUpdateResponse>
                ) {
                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        // Show toast message to the user
                        Toast.makeText(requireContext(), "Your password has been updated successfully.", Toast.LENGTH_LONG).show()
                        // Clear text from password fields
                        passwordEditText.setText("")
                        confPasswordEditText.setText("")
                        // Clear focus from password fields
                        passwordEditText.clearFocus()
                        confPasswordEditText.clearFocus()

                    } else {
                        Toast.makeText(requireContext(), "Message: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProfileUpdateResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }

        // Doctors
        fun apiDoctors() {

            val apiCall = RetrofitClient.getRetrofitInstance.doctors(token = "JWT ${sessionManager.fetchAuthToken()}")

            apiCall.enqueue(object : Callback<List<Doctors>?> {
                override fun onResponse(
                    call: Call<List<Doctors>?>,
                    response: Response<List<Doctors>?>
                ) {

                    val statusCode = response.raw().code()

                    if (statusCode == 200) {
                        val responseBody = response.body()!!

                        Log.d("UserProfile", "$responseBody")

                        responseBody.forEach{
                            if (it.specialty == "Family Doctor") {
                                familyDoctorsIds.add(it._id)
                                familyDoctorsFirstNames.add(it.firstName)
                                familyDoctorsLastNames.add(it.lastName)
                                familyDoctorsFullNames.add("${it.firstName} ${it.lastName}")
                            }
                        }

                        // Family Doctor select field
                        val familyDoctorInputLayout : TextInputLayout = view.findViewById(R.id.FamilyDoctorInput)

                        val items1 = familyDoctorsFullNames.toList()
                        val adapter1 = ArrayAdapter(requireContext(), R.layout.list_item, items1)
                        (familyDoctorInputLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter1)

                    }


                }

                override fun onFailure(call: Call<List<Doctors>?>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })

        }

        apiDoctors()






        familyDoctorEditText.setOnItemClickListener { parent, view, position, id ->
            itemPosition = position
        }


        // Validate profile form with vvalidator library
        form {
            inputLayout(view.findViewById(R.id.HealthIdInput)) {
                isNotEmpty().description("Please enter your health ID number.")
                isNumber().description("Health ID must be only numbers.")
                length().exactly(11).description("Health ID must be exactly 11 digits.")
            }
            inputLayout(view.findViewById(R.id.EmailInput)) {
                isNotEmpty().description("Please enter your email address.")
                isEmail().description("Please enter a valid email address.")
            }
            submitWith(view.findViewById(R.id.textButton)) { result ->
                // Button was clicked and form is completely valid!

                // Retrieve text from all profile fields
                val healthIdText = healthIdEditText.text.toString().trim()
                val firstNameText = firstNameEditText.text.toString().trim()
                val lastNameText = lastNameEditText.text.toString().trim()
                val emailText = emailEditText.text.toString().trim()
                val phoneNumberText = phoneNumberEditText.text.toString().trim()
                val bloodTypeText = bloodTypeEditText.text.toString().trim()
                val birthDateText = birthDateEditText.text.toString()

                // Assign text
                healthIdNumber = healthIdText
                if (firstNameText != "") firstName = firstNameText
                if (lastNameText != "") lastName = lastNameText
                email = emailText
                if (phoneNumberText != "") phoneNumber = phoneNumberText
                if (bloodTypeText != "") bloodType = bloodTypeText
                if (birthDateText != "") {
                    if (datePickerSelection != null) {
                        // var testV = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date(datePicker.selection!!))
                        birthDate = dateFormatter.appDateToApiDate(birthDateText)
                    }
                }
                if (itemPosition != null) {
                    familyDoctorId = familyDoctorsIds[itemPosition!!]
                    familyDoctorFirstName = familyDoctorsFirstNames[itemPosition!!]
                    familyDoctorLastName = familyDoctorsLastNames[itemPosition!!]
                }


                Log.d("ProfileFragment", "$healthIdNumber, $firstName, $lastName, $email, $phoneNumber, $bloodType, $birthDate, $familyDoctorId, $familyDoctorFirstName, $familyDoctorLastName")



                // Call Api
                apiProfileUpdate()

            }

        }

        // Validate password form with vvalidator library
        form {
            inputLayout(view.findViewById(R.id.PasswordInput)) {
                isNotEmpty().description("Please enter a new password.")
                length().atLeast(8).description("Your password must be at least 8 characters.")
            }
            inputLayout(view.findViewById(R.id.ConfirmPasswordInput)) {
                isNotEmpty().description("Please confirm your new password.")
                compareWith(view.findViewById(R.id.PasswordInput)).description("Password does not match.")

            }
            submitWith(view.findViewById(R.id.passwordBtn)) { result ->
                // Button was clicked and form is completely valid!
                password = passwordEditText.text.toString().trim()

                // Call Api
                apiPasswordUpdate()
            }
        }





        return view


    }
    
}