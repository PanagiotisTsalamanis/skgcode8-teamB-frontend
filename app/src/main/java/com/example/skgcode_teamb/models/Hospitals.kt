package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class Hospitals(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("departments")
    val departments: List<String>,
    @SerializedName("city")
    val city: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("__v")
    val __v: Int
)