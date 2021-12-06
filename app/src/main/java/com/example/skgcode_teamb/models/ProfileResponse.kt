package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("bloodType")
    val bloodType: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("familyDoctorId")
    val familyDoctorId: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("healthIdNumber")
    val healthIdNumber: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("__v")
    val v: Int
)