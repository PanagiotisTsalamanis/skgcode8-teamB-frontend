package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("healthIdNumber")
    val healthIdNumber: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)