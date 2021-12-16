package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Appointments(
    @SerializedName("doctor")
    val doctor: Doctor,
    @SerializedName("hospital")
    val hospital: Hospital,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("__v")
    val __v: Int
)