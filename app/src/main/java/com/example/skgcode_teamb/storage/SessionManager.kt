package com.example.skgcode_teamb.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.skgcode_teamb.R
import com.example.skgcode_teamb.models.LoginResponse
import com.example.skgcode_teamb.models.ProfileUpdateRequest

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (var context: Context?) {
    private var prefs: SharedPreferences = context!!.getSharedPreferences("health_app", Context.MODE_PRIVATE)

    companion object {
        // All Shared Preferences Keys
        const val IS_LOGIN = "IsLoggedIn"
        const val USER_TOKEN = "user_token"
        const val HEALTH_ID = "health_id"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val EMAIL = "email"
        const val PHONE_NUMBER = "phone_number"
        const val BIRTH_DATE = "birth_date"
        const val BLOOD_TYPE = "blood_type"
        const val FAMILY_DOCTOR_ID = "family_doctor_id"
        const val FAMILY_DOCTOR_FIRST_NAME = "family_doctor_first_name"
        const val FAMILY_DOCTOR_LAST_NAME = "family_doctor_last_name"
    }

    /**
     * Create login session
     */
    fun createSession(loginResponse: LoginResponse) {

        // Editor for Share Preferences
        val editor = prefs.edit()

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // Storing auth token in pref
        editor.putString(USER_TOKEN, loginResponse.accessToken)

        // Storing all user data in pref
        editor.putString(HEALTH_ID, loginResponse.user.healthIdNumber)
        editor.putString(FIRST_NAME, loginResponse.user.firstName)
        editor.putString(LAST_NAME, loginResponse.user.lastName)
        editor.putString(EMAIL, loginResponse.user.email)
        editor.putString(PHONE_NUMBER, loginResponse.user.phoneNumber)
        editor.putString(BIRTH_DATE, loginResponse.user.birthDate)
        editor.putString(BLOOD_TYPE, loginResponse.user.bloodType)
        editor.putString(FAMILY_DOCTOR_ID, loginResponse.user.familyDoctor.id)
        editor.putString(FAMILY_DOCTOR_FIRST_NAME, loginResponse.user.familyDoctor.firstName)
        editor.putString(FAMILY_DOCTOR_LAST_NAME, loginResponse.user.familyDoctor.lastName)

        // Apply changes
        editor.apply()
    }


    fun updateSession(profileUpdateRequest: ProfileUpdateRequest) {

        // Editor for Share Preferences
        val editor = prefs.edit()

        // Updating user data in pref
        editor.putString(HEALTH_ID, profileUpdateRequest.healthIdNumber)
        editor.putString(FIRST_NAME, profileUpdateRequest.firstName)
        editor.putString(LAST_NAME, profileUpdateRequest.lastName)
        editor.putString(EMAIL, profileUpdateRequest.email)
        editor.putString(PHONE_NUMBER, profileUpdateRequest.phoneNumber)
        editor.putString(BIRTH_DATE, profileUpdateRequest.birthDate)
        editor.putString(BLOOD_TYPE, profileUpdateRequest.bloodType)
        editor.putString(FAMILY_DOCTOR_ID, profileUpdateRequest.familyDoctor?.id)
        editor.putString(FAMILY_DOCTOR_FIRST_NAME, profileUpdateRequest.familyDoctor?.firstName)
        editor.putString(FAMILY_DOCTOR_LAST_NAME, profileUpdateRequest.familyDoctor?.lastName)

        // Apply changes
        editor.apply()
    }


    /**
     * Create login session

    fun createLoginSession(
        token: String?, health_id: String?, first_name: String?, last_name: String?, email: String?,
        phone_number: String?, birth_date: String?, blood_type: String?, familyd_id: String?) {

        // Editor for Share Preferences
        val editor = prefs.edit()

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // Storing auth token in pref
        editor.putString(USER_TOKEN, token)

        // Storing all user data
        editor.putString(HEALTH_ID, health_id)
        editor.putString(FIRST_NAME, first_name)
        editor.putString(LAST_NAME, last_name)
        editor.putString(EMAIL, email)
        editor.putString(PHONE_NUMBER, phone_number)
        editor.putString(BIRTH_DATE, birth_date)
        editor.putString(BLOOD_TYPE, blood_type)
        editor.putString(FAMILY_DOCTOR_ID, familyd_id)

        // Apply changes
        editor.apply()
    } */


    /**
     * Function to retrieve session data
     */
    fun getUserDetails(): SessionUserDetails {
        val healthIdNumber = prefs.getString(HEALTH_ID, null)
        val firstName = prefs.getString(FIRST_NAME, null)
        val lastName = prefs.getString(LAST_NAME, null)
        val email = prefs.getString(EMAIL, null)
        val phoneNumber = prefs.getString(PHONE_NUMBER, null)
        val birthDate = prefs.getString(BIRTH_DATE, null)
        val bloodType = prefs.getString(BLOOD_TYPE, null)
        val familyDoctorId = prefs.getString(FAMILY_DOCTOR_ID, null)
        val familyDoctorFirstName = prefs.getString(FAMILY_DOCTOR_FIRST_NAME, null)
        val familyDoctorLastName = prefs.getString(FAMILY_DOCTOR_LAST_NAME, null)

        return SessionUserDetails(healthIdNumber, firstName, lastName, email, phoneNumber, birthDate, bloodType, familyDoctorId, familyDoctorFirstName, familyDoctorLastName)
    }


    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }


    /**
     * Function to clear session details
     */
    fun clearSession() {
        // Clearing all user data from SharedPreferences
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Get login status
     */
    val isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)

}