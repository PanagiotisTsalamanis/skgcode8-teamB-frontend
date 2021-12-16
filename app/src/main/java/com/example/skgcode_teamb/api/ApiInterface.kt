package com.example.skgcode_teamb.api

import android.text.Html
import com.example.skgcode_teamb.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

    // Prescriptions: https://skgcode8-teamb-backend.herokuapp.com/services/prescriptions
    @GET("services/prescriptions")
    fun userPrescriptions(
        @Header("Authorization") token: String
    ): Call<List<Prescription>>

    // Appointments history: https://skgcode8-teamb-backend.herokuapp.com/services/appointments
    @GET("appointments")
    fun userAppointments(
        @Header("Authorization") token: String
    ): Call<List<Appointments>>

    // All hospitals: https://skgcode8-teamb-backend.herokuapp.com/hospitals
    @GET("hospitals")
    fun hospitals(
        @Header("Authorization") token: String
    ): Call<List<Hospitals>>

}