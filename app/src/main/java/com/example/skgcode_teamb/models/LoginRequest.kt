package com.example.skgcode_teamb.models

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("healthIdNumber")
    val healthIdNumber: String,
    @SerializedName("password")
    val password: String
)