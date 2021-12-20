package com.example.skgcode_teamb.api

import android.text.Html
import com.example.skgcode_teamb.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // Register: https://skgcode8-teamb-backend.herokuapp.com/register
    @POST("register")
    fun userRegister(
        @Body registerRequest: RegisterRequest
    ): Call<Html> // isws na iparxei lathos sto Html

    // User Login: https://skgcode8-teamb-backend.herokuapp.com/login
    @POST("login")
    fun userLogin(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    // User Profile: https://skgcode8-teamb-backend.herokuapp.com/profile
    @GET("profile")
    fun userProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    // Profile update: https://skgcode8-teamb-backend.herokuapp.com/profile
    @PUT("profile")
    fun userProfileUpdate(
        @Header("Authorization") token: String,
        @Body profileUpdateRequest: ProfileUpdateRequest
    ): Call<ProfileUpdateResponse>

    // Password Change: https://skgcode8-teamb-backend.herokuapp.com/profile
    @PUT("profile")
    fun userPasswordUpdate(
        @Header("Authorization") token: String,
        @Body passwordUpdateRequest: PasswordUpdateRequest
    ): Call<ProfileUpdateResponse>

    // Prescriptions history: https://skgcode8-teamb-backend.herokuapp.com/services/prescriptions
    @GET("services/prescriptions")
    fun userPrescriptions(
        @Header("Authorization") token: String
    ): Call<List<Prescription>>

    // Diagnoses history: https://skgcode8-teamb-backend.herokuapp.com/services/diagnoses
    @GET("services/diagnoses")
    fun userDiagnoses(
        @Header("Authorization") token: String
    ): Call<List<Diagnosis>>

    // Appointments history: https://skgcode8-teamb-backend.herokuapp.com/services/appointments
    @GET("services/appointments")
    fun userAppointments(
        @Header("Authorization") token: String
    ): Call<List<Appointments>>

    // All hospitals: https://skgcode8-teamb-backend.herokuapp.com/hospitals
    @GET("hospitals")
    fun hospitals(
        @Header("Authorization") token: String
    ): Call<List<Hospitals>>

    // All doctors: https://skgcode8-teamb-backend.herokuapp.com/doctors
    @GET("doctors")
    fun doctors(
        @Header("Authorization") token: String
    ): Call<List<Doctors>>

    // Appointments Search Availability: https://skgcode8-teamb-backend.herokuapp.com/services/appointments/available
    @GET("services/appointments/available")
    fun searchAppointment(
        @Header("Authorization") token: String,
        @Query("hospitalId") hospitalId: String,
        @Query("department") department: String,
        @Query("date") date: String
    ): Call<List<String>>

    // Schedule Appointment: https://skgcode8-teamb-backend.herokuapp.com/services/appointments
    @POST("services/appointments")
    fun scheduleAppointment(
        @Header("Authorization") token: String,
        @Body scheduleAppRequest: ScheduleAppRequest
    ): Call<ScheduleAppResponse>

}