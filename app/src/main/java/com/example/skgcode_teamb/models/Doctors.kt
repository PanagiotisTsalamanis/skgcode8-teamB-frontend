package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Doctors(
    @SerializedName("hospital")
    val hospital: Hospital,
    @SerializedName("_id")
    val _id: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("specialty")
    val specialty: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("__v")
    val __v: Int
)