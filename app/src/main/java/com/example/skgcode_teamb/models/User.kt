package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("bloodType")
    val bloodType: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("familyDoctor")
    val familyDoctor: Doctor?,
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